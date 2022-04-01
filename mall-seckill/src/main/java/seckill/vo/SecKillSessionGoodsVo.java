package seckill.vo;

import lombok.Data;
import seckill.domain.SecKillGoods;

import java.util.Date;
import java.util.List;

/**
 * 将场次和商品关联起来
 */
@Data
public class SecKillSessionGoodsVo {

    private List<SecKillGoods> secKillGoods;
    private Date endTime;
    private Long placeId;
    private Long eventId;
}
