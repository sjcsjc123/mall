package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeGoodsNumberLog implements Serializable {

    private Long goodsId;
    private Long orderId;
    private int goodsNumber;
    private Date logTime;

}
