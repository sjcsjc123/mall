package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TradeShipping implements Serializable {
    private Long orderId;
    private int shippingStatus;
    private Long shippingId;
}
