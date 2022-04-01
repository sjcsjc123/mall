package api.order;

import common.domian.TradeOrder;
import common.mqEntity.OrderMessage;

/**
 * 用于订单微服务的一系列操作
 */
public interface OrderService {

    /**
     * 创建订单
     */
    void insert(TradeOrder order);

    /**
     * 取消未支付订单
     */
    void chancelNoPaidOrder(OrderMessage orderMessage);

    /**
     * 取消已支付订单
     */
    void chancelPaidOrder(OrderMessage orderMessage);

    /**
     * 支付订单
     */
    void payOrder(Long orderId);
}
