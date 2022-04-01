package common.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerMoneyLog implements Serializable {
    private Long id;
    private Long orderId;
    private int moneyLogType;
    private float useMoney;
    private Date createTime;
}
