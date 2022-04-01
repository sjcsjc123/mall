package common.vo;

import lombok.Data;

/**
 * 封装删除购物车需要的数据
 */
@Data
public class CartIdVo {
    private Long skuId;
    private Long spuId;
}
