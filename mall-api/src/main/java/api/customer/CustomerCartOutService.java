package api.customer;

import common.vo.CustomerCartVo;

import java.util.List;

/**
 * 未登录状态的购物车系统
 */
public interface CustomerCartOutService {

    /**
     * 添加购物车
     */
    void create(Long goodsId,int goodsNumber);

    /**
     * 更改购物车
     */
    void updateGoods(Long goodsId,int goodsNumber);

    /**
     * 批量删除购物车
     */
    void delete(List<Long> goodsIds);

    /**
     * 查看购物车
     */
    List<CustomerCartVo> select();
}
