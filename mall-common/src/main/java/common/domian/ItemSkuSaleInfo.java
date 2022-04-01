package common.domian;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class ItemSkuSaleInfo implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    private Long spuId;
    /**
     * attr_id
     */
    private Long attrId;
    /**
     * 销售属性名
     */
    private String attrName;
    /**
     * 销售属性值
     */
    private String attrValue;
}
