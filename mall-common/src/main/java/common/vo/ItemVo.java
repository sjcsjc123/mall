package common.vo;

import common.domian.ItemSkuSaleInfo;
import common.domian.ItemSpuCommentInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品的详细信息
 */
@Data
public class ItemVo implements Serializable {
    private Long skuId;
    private float skuPrice;
    private List<ItemSkuSaleInfo> skuSaleInfos;
    private String spuName;
    private String spuDesc;
    private String desc;
    private int saleCount;
    private List<ItemSpuCommentInfo> spuCommentInfos;
}
