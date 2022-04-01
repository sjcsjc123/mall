package common.domian;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品评价
 */
@Data
public class ItemSpuCommentInfo implements Serializable {

    @TableId
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * spu_id
     */
    private Long spuId;
    /**
     * 商品名字
     */
    private String spuName;
    /**
     * 会员昵称
     */
    private String customerName;
    /**
     * 会员ip
     */
    private String customerId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 内容
     */
    private String content;
}
