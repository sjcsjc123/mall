package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradePay implements Serializable {
    private Long payId;
    private Long orderId;
    private float payAmount;
    private int isPaid;
}
