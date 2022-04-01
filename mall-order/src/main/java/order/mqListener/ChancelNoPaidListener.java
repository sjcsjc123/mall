package order.mqListener;

import api.coupon.CouponService;
import api.customer.CustomerService;
import api.goods.GoodsService;
import api.order.OrderService;
import order.mapper.TradeOrderMapper;
import common.mqEntity.OrderMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;

@RocketMQMessageListener(topic = "chancelNoPaidTopic",consumerGroup =
        "chancelOrderGroup",messageModel = MessageModel.CLUSTERING)
public class ChancelNoPaidListener implements RocketMQListener<OrderMessage> {

    @Autowired
    private TradeOrderMapper orderMapper;
    @DubboReference
    private GoodsService goodsService;
    @DubboReference
    private CustomerService customerService;
    @DubboReference
    private CouponService couponService;
    @Autowired
    private OrderService orderService;

    @Override
    public void onMessage(OrderMessage orderMessage) {
        //接收到取消未支付订单消息
        orderService.chancelNoPaidOrder(orderMessage);
    }
}
