package api.customer;

import common.vo.CartIdVo;
import common.vo.CustomerCartItemVo;
import common.vo.CustomerCartVo;

import java.util.List;

/**
 * 消费者购物车系统
 */
public interface CustomerCartService {

    /**
     * 添加购物车
     */
    CustomerCartItemVo insert(String username, Long skuId,Long spuId,
                              int goodsNumber);

    /**
     * 批量删除购物车
     */
    void delete(String username, List<CartIdVo> cartIdVos);

    /**
     * 更改商品数量
     */
    void updateGoodsNumber(String userKey,CartIdVo cartIdVo,int goodsNumber);

    /**
     * 获取全部购物车
     */
    CustomerCartVo select();

    /**
     * 查看购物车某一商品
     */
    CustomerCartItemVo selectOne(CartIdVo cartIdVo, String userKey);
}
