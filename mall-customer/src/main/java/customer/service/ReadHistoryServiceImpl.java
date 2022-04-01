package customer.service;

import api.goods.ShopGoodsService;
import api.customer.ReadHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.domian.Customer;
import common.domian.ShopGoods;
import common.vo.ReadHistoryVo;
import customer.mapper.CustomerMapper;
import customer.mapper.ReadHistoryMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService
@Component
public class ReadHistoryServiceImpl implements ReadHistoryService {

    @Autowired
    private ReadHistoryMapper readHistoryMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @DubboReference
    private ShopGoodsService shopGoodsService;

    @Override
    public void createHistory(String username, Long goodsId) {
        ReadHistoryVo readHistoryVo = new ReadHistoryVo();
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_name",username);
        Customer customer = customerMapper.selectOne(wrapper);
        ShopGoods shopGoods = shopGoodsService.select(goodsId);
        //从浏览记录查找是否存在相同的商品，如果有，则更新浏览时间
        ReadHistoryVo history =
                readHistoryMapper.findByGoodsIdAndCustomerName(goodsId,
                        username);
        if (history != null){
            history.setCreateTime(new Date());
            readHistoryMapper.save(history);
        }else {
            readHistoryVo.setCreateTime(new Date());
            readHistoryVo.setCustomerName(customer.getCustomerName());
            readHistoryVo.setUserId(customer.getId());
            readHistoryVo.setGoodsId(goodsId);
            readHistoryVo.setGoodsName(shopGoods.getGoodsName());
            readHistoryVo.setGoodsDesc(shopGoods.getGoodsDesc());
            readHistoryVo.setGoodsPrice(shopGoods.getGoodsPrice());
            readHistoryMapper.insert(readHistoryVo);
        }
    }

    @Override
    public Page<ReadHistoryVo> findByIdAndTime(String username, int pageNum,
                                               int pageSize) {
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        return readHistoryMapper
                .findAllByCustomerNameOrderByCreateTimeDesc(username, pageable);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        List<ReadHistoryVo> readHistories = new ArrayList<>();
        for (String id : ids) {
            ReadHistoryVo readHistoryVo = new ReadHistoryVo();
            readHistoryVo.setId(id);
            readHistories.add(readHistoryVo);
        }
        readHistoryMapper.deleteAll(readHistories);
    }

    @Override
    public void clear(String username) {
        readHistoryMapper.deleteAllByCustomerName(username);
    }
}
