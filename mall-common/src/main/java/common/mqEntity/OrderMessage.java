package common.mqEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessage {
    private Long id;
    private Long orderId;
    private Long couponId;
    private Long goodsId;
    private float moneyPaid;
    private int goodsNumber;
}
