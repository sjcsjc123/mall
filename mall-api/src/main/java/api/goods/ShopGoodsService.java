package api.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import common.domian.ShopGoods;

import java.util.List;

/**
 * 商家对shop—goods表的操作
 */
public interface ShopGoodsService {

    /**
     * 添加商品
     */
    void insert(ShopGoods shopGoods);

    /**
     * 更改商品信息
     */
    void update(ShopGoods shopGoods);

    /**
     * 下架商品
     */
    void delete(Long goodsId);

    /**
     * 查找单个商品
     */
    ShopGoods select(Long goodsId);

    /**
     * 查找商家名下所有商品
     */
    IPage<ShopGoods> selectByBusinessName(String username);

    /**
     * 查找店铺下所有商品
     */
    IPage<ShopGoods> selectByShopId(Long shopId);
}
