package api.goods;

import common.domian.TradeGoods;
import common.domian.TradeGoodsNumberLog;

/**
 * 用于商品微服务的一系列操作
 */
public interface GoodsService {

    /**
     * 添加商品
     */
    void insert(TradeGoods goods);

    /**
     * 更改商品
     */
    void update(TradeGoods goods);

    /**
     * 删除商品
     */
    void delete(Long goodsId);

    /**
     * 扣减库存
     */
    void reduceGoodsNumber(TradeGoodsNumberLog tradeGoodsNumberLog);

    /**
     * 回增库存
     */
    void chancelReduce(Long goodsId,int goodsNumber);

    /**
     * 根据商品id查找商品
     */
    TradeGoods selectOne(Long goodsId);
}
