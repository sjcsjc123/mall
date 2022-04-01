package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TradeOrder implements Serializable {
    private Long orderId;
    private Long id;
    private int orderStatus;
    private int payStatus;
    private String address;
    private String consignee;
    private Long goodsId;
    private int goodsNumber;
    private float goodsPrice;
    private float goodsAmount;
    private float shippingFee;
    private float orderAmount;
    private Long couponId;
    private float couponPaid;
    private float moneyPaid;
    private float payAmount;
    private Date addTime;
    private Date confirmTime;
    private Date payTime;
    private int moneyStatus;
    private Long shippingId;
}
