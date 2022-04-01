package coupon.service;

import api.coupon.CouponService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.TradeCoupon;
import common.exception.MyProjectException;
import common.util.IDWorker;
import coupon.mapper.CouponMapper;
import coupon.mqEntity.CouponMessage;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@DubboService
@Component
public class CouponServiceImpl implements CouponService {

    private final static Long EXPIRATION = 1000*60*60*24*1000L;
    private final static String TOPIC = "deleteCouponTopic";
    private final static String TAG = "couponTag";

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private IDWorker idWorker;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Bean
    public IDWorker idWorker(){
        return new IDWorker(1,3);
    }

    /**
     * 需要给coupon传入 userId 参数,useTime和 orderId 默认为空
     * @param coupon
     */
    @Override
    public void insert(TradeCoupon coupon) {
        coupon.setCouponId(idWorker.nextId());
        Date nowTime = new Date();
        coupon.setExpireTime(new Date(nowTime.getTime() + EXPIRATION));
        try {
            //给rocketMq发送延迟消息，到期后自动删除优惠券
            CouponMessage couponMessage = new CouponMessage();
            couponMessage.setCouponId(coupon.getCouponId());
            couponMessage.setExpireTime(coupon.getExpireTime());
            couponMessage.setUserId(coupon.getUserId());
            Message message = new Message(TOPIC,TAG,couponMessage.toString().getBytes(StandardCharsets.UTF_8));
            message.setDelayTimeLevel(21);
            rocketMQTemplate.getProducer().setRetryTimesWhenSendFailed(10);
            rocketMQTemplate.getProducer().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //可以举行各种各样的活动来发放优惠券，此处是无门槛五块钱优惠券
        coupon.setCouponPrice(5f);
        coupon.setIsUsed(0);
        couponMapper.insert(coupon);
    }

    @Override
    public void delete(TradeCoupon coupon) {
        //如果已经到期并且还未使用
        if (coupon.getExpireTime().before(new Date()) && coupon.getIsUsed() == 0){
            couponMapper.deleteById(coupon.getCouponId());
        }
    }

    /**
     * 传入orderId参数
     * @param coupon
     */
    @Override
    public void useCoupon(TradeCoupon coupon) {
        if (coupon.getExpireTime().before(new Date()) || coupon.getIsUsed() == 1){
            throw new MyProjectException(MyProjectExceptionEnum.COUPON_IS_NULL);
        }
        coupon.setIsUsed(1);
        coupon.setUsedTime(new Date());
        QueryWrapper<TradeCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("coupon_id",coupon.getCouponId());
        couponMapper.update(coupon,wrapper);
    }

    @Override
    public TradeCoupon selectOne(Long couponId) {
        QueryWrapper<TradeCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("coupon_id",couponId);
        return couponMapper.selectOne(wrapper);
    }

    @Override
    public void updateCouponStatus(TradeCoupon coupon) {
        QueryWrapper<TradeCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("coupon_id",coupon.getCouponId());
        couponMapper.update(coupon,wrapper);
    }
}
