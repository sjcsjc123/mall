package seckill.vo;

import lombok.Data;
import seckill.domain.SecKillGoods;

import java.util.List;

@Data
public class SecKillGoodsVo {

    private String token;
    private SecKillGoods secKillGoods;
}
