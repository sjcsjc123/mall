package common.domian;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {
    @TableId
    private Long id;
    private String customerName;
    private String customerPhone;
    private String customerPassword;
    private float customerMoney;
    private int customerScore;
    private Date customerRegTime;
    private Date customerLastLogin;
    private int customerStatus;
    private int status;
}
