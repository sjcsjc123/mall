package elasticsearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.ItemSkuInfo;
import common.domian.ItemSkuSaleInfo;
import common.domian.ItemSpuCommentInfo;
import common.domian.ItemSpuInfo;
import common.exception.MyProjectException;
import common.vo.ItemVo;
import elasticsearch.config.ThreadConfig;
import elasticsearch.mapper.ItemSkuMapper;
import elasticsearch.mapper.ItemSkuSaleMapper;
import elasticsearch.mapper.ItemSpuCommentMapper;
import elasticsearch.mapper.ItemSpuMapper;
import elasticsearch.service.ItemService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@DubboService
@Component
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemSpuMapper spuMapper;
    @Autowired
    private ItemSkuMapper skuMapper;
    @Autowired
    private ItemSkuSaleMapper saleMapper;
    @Autowired
    private ItemSpuCommentMapper commentMapper;
    @Autowired
    private ThreadConfig threadConfig;

    @Override
    public List<ItemVo> showBySpuId(Long spuId) {
        System.out.println("开始编排。。。。。");
        List<ItemSkuInfo> skuInfos =
                skuMapper.selectList(new QueryWrapper<ItemSkuInfo>().eq(
                        "spu_id", spuId));
        ArrayList<ItemVo> itemVos = new ArrayList<>();
        for (ItemSkuInfo skuInfo : skuInfos) {
            ItemVo itemVo = compThread(spuId, skuInfo.getSkuId());
            itemVos.add(itemVo);
        }

        return itemVos;
    }

    @Override
    public ItemVo showBySpuIdAndSkuId(Long spuId, Long skuId) {
        return compThread(spuId,skuId);
    }

    /**
     * 将异步线程编排抽取出来
     */
    private ItemVo compThread(Long spuId,Long skuId){
        ThreadPoolExecutor executor =
                threadConfig.threadPoolExecutor();
        ItemVo itemVo = new ItemVo();
        CompletableFuture<Void> future1 =
                CompletableFuture.runAsync(() -> {
                    //获取商品spu信息并存入itemVo
                    System.out.println("任务一开始。。。。");
                    ItemSpuInfo itemSpuInfo = spuMapper.selectById(spuId);
                    itemVo.setSpuName(itemSpuInfo.getSpuName());
                    itemVo.setSpuDesc(itemSpuInfo.getSpuDesc());
                    itemVo.setDesc(itemSpuInfo.getDetailDesc());
                    itemVo.setSaleCount(itemSpuInfo.getSaleCount());
                }, executor);
        CompletableFuture<Void> future2 =
                CompletableFuture.runAsync(() -> {
                    //获取sku信息并存入itemVo,获取商品销售属性并存入itemVo
                    System.out.println("任务二开始。。。。");
                    itemVo.setSkuId(skuId);
                    List<ItemSkuSaleInfo> itemSkuSaleInfos =
                            saleMapper.selectList(new QueryWrapper<ItemSkuSaleInfo>().eq(
                                    "sku_id", skuId));
                    itemVo.setSkuSaleInfos(itemSkuSaleInfos);
                }, executor);
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            //获取商品评价并存入itemVo
            System.out.println("任务三开始。。。。");
            List<ItemSpuCommentInfo> itemSpuCommentInfos =
                    commentMapper.selectList(new QueryWrapper<ItemSpuCommentInfo>().eq(
                            "spu_id", spuId));
            itemVo.setSpuCommentInfos(itemSpuCommentInfos);
        }, executor);
        CompletableFuture<Void> future4 =
                CompletableFuture.runAsync(() -> {
            System.out.println("任务四开始。。。");
            float skuPrice = skuMapper.selectSkuPrice(skuId, spuId);
            itemVo.setSkuPrice(skuPrice);
        }, executor);
        try {
            CompletableFuture.allOf(future1, future2, future3,future4).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_ENTAIL_ERROR);
        }
        return itemVo;
    }
}
