package common.vo;

import lombok.Data;

/**
 * 封装商品的属性
 */
@Data
public class ItemAttrVo {
    private Long attrId;
    private String attrName;
    private String attrValue;
}
