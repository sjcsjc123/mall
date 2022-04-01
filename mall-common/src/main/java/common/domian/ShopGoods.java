package common.domian;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopGoods implements Serializable {

    private String goodsName;
    private Long shopId;
    @TableId
    private Long goodsId;
    private float goodsPrice;
    private String goodsDesc;
    private int goodsSold;
    private int goodsNumber;
    private String businessName;

}
