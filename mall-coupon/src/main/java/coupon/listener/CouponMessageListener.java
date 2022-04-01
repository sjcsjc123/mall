package coupon.listener;

import api.coupon.CouponService;
import common.domian.TradeCoupon;
import coupon.mapper.CouponMapper;
import coupon.mqEntity.CouponMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = "deleteCouponTopic",consumerGroup =
        "mall-coupon",messageModel = MessageModel.CLUSTERING)
@Component
@Slf4j
public class CouponMessageListener implements RocketMQListener<CouponMessage> {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponMapper couponMapper;

    @Override
    public void onMessage(CouponMessage couponMessage) {
        log.info("接收到优惠券过期消息"+couponMessage);
        TradeCoupon tradeCoupon =
                couponMapper.selectById(couponMessage.getCouponId());
        couponService.delete(tradeCoupon);
    }
}
