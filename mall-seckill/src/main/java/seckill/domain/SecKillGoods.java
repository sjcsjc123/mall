package seckill.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecKillGoods implements Serializable {

    @TableId
    private Long id;
    private Long placeId;
    private Long eventId;
    private Long spuId;
    private Long skuId;
    private int goodsNumber;
    private float skuPrice;
    private String goodsName;
    private String goodsDesc;

}
