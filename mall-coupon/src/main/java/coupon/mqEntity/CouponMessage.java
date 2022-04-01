package coupon.mqEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 发送删除优惠券消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponMessage {
    private Long couponId;
    private Long userId;
    private Date expireTime;
}
