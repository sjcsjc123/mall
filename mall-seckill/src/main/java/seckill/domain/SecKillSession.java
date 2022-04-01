package seckill.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 秒杀活动信息
 * 一个活动有多个秒杀商品，每个秒杀商品对应唯一一个placeId
 */
@Data
public class SecKillSession {

    @TableId
    private Long id;
    /**
     * 活动id
     */
    private Long eventId;
    /**
     * 场次id
     */
    private Long placeId;
    private String placeName;
    private Date startTime;
    private Date endTime;

}
