package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeCoupon implements Serializable {
    private Long couponId;
    private float couponPrice;
    private Long userId;
    private Long orderId;
    private int isUsed;
    private Date usedTime;
    private Date expireTime;
}
