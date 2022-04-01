package api.seckill;

/**
 * 商品秒杀
 */
public interface SecKillService {

    /**
     * 开始秒杀
     */
    void beginSecKill(Long goodsId) throws InterruptedException;

    /**
     * 查询商品现有库存
     */
    int selectGoodsNumber(Long goodsId);

    void uploadSecKillGoods();
}
