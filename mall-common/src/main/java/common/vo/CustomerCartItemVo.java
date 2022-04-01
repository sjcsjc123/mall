package common.vo;

import lombok.Data;

import java.util.List;

/**
 * 消费者购物车中商品的详情
 */
@Data
public class CustomerCartItemVo {

    private Long spuId;
    private Long skuId;
    private String goodsName;
    private int goodsNumber;
    private float goodsAmount;
    private List<ItemAttrVo> itemAttrVos;
    private float skuPrice;

}
