package api.coupon;

import common.domian.TradeCoupon;

/**
 * 用于优惠券服务的一系列操作
 */
public interface CouponService {

    /**
     * 发放优惠券
     */
    void insert(TradeCoupon coupon);

    /**
     * 删除优惠券
     */
    void delete(TradeCoupon coupon);

    /**
     * 使用优惠券
     */
    void useCoupon(TradeCoupon coupon);

    /**
     * 查看优惠券
     */
    TradeCoupon selectOne(Long couponId);

    void updateCouponStatus(TradeCoupon coupon);
}
