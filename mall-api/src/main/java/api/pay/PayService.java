package api.pay;

/**
 * 用于支付微服务的一系列操作
 */
public interface PayService {

    /**
     * 创建支付订单
     */
    void createPay(Long orderId,float payAmount);

    /**
     * 取消支付订单
     */
    void chancel(Long orderId);
}
