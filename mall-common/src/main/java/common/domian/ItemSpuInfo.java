package common.domian;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * sku 与 spu 是一对一
 * spu相当于进入商品前的界面的id
 * sku相当于进入商品后的界面的id
 * 一个sku可以有多个attr id 即商品规格的id
 * 一个goodsId可以有多个sku_id
 * 一个sku可以有多个评价，即对应多个ItemSpuCommentInfo的id
 * spu与shopGoods之间的关系暂定
 */
@Data
public class ItemSpuInfo implements Serializable {

    @TableId
    private Long spuId;
    private String spuName;
    private String spuDesc;
    private String detailDesc;
    private int saleCount;

}
