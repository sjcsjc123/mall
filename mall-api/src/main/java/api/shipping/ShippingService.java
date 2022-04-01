package api.shipping;

/**
 * 用于快递微服务的一系列操作
 */
public interface ShippingService {

    /**
     * 创建快递单号
     */
    Long createShipping(Long orderId);

    /**
     * 更改订单快递状态
     */
    void updateShippingStatus(Long orderId,int shippingStatus);

    /**
     * 删除快递订单
     */
    void delete(Long orderId);
}
