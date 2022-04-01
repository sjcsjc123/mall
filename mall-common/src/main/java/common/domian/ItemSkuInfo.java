package common.domian;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class ItemSkuInfo implements Serializable {
    @TableId
    private Long id;
    private Long skuId;
    private Long spuId;
    private float skuPrice;

}
