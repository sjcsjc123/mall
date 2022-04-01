package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TradeGoods implements Serializable {
    private Long goodsId;
    private String goodsName;
    private int goodsNumber;
    private float goodsPrice;
    private String goodsDesc;
    private Date addTime;
    private String businessName;
}
