package goods.service;

import api.goods.ShopGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import common.domian.ShopGoods;
import common.util.IDWorker;
import goods.mapper.ShopGoodsMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@DubboService
public class ShopGoodsServiceImpl implements ShopGoodsService {

    @Autowired
    private ShopGoodsMapper shopGoodsMapper;
    @Autowired
    private IDWorker idWorker;

    @Override
    public void insert(ShopGoods shopGoods) {
        shopGoods.setGoodsId(idWorker.nextId());
        shopGoodsMapper.insert(shopGoods);
    }

    @Override
    public void update(ShopGoods shopGoods) {
        shopGoodsMapper.updateById(shopGoods);
    }

    @Override
    public void delete(Long goodsId) {
        shopGoodsMapper.deleteById(goodsId);
    }

    @Override
    public ShopGoods select(Long goodsId) {
        return shopGoodsMapper.selectById(goodsId);
    }

    @Override
    public IPage<ShopGoods> selectByBusinessName(String username) {
        IPage<ShopGoods> page = new Page<>();
        page.setPages(10);
        page.setSize(10);
        QueryWrapper<ShopGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("business_name",username);
        return shopGoodsMapper.selectPage(page,wrapper);
    }

    @Override
    public IPage<ShopGoods> selectByShopId(Long shopId) {
        IPage<ShopGoods> page = new Page<>();
        page.setPages(10);
        page.setSize(10);
        QueryWrapper<ShopGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id",shopId);
        return shopGoodsMapper.selectPage(page,wrapper);
    }
}
