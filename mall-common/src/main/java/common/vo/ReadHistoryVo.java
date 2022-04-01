package common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 消费者浏览记录
 */
@Document(value = "history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadHistoryVo {
    @Id
    private String id;
    @Indexed
    private Long userId;
    private Long goodsId;
    private String customerName;
    private String goodsName;
    private String goodsDesc;
    private float goodsPrice;
    private Date createTime;
}
