package elasticsearch.EsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * elastic search所需要的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "mall")
public class EsGoods implements Serializable {
    @Id
    private Long goodsId;
    @Field(type = FieldType.Keyword)
    private String goodsDesc;
    @Field(analyzer = "ik_max_word",type = FieldType.Text)
    private String goodsName;
    @Field(type = FieldType.Float)
    private float goodsPrice;
    @Field(type = FieldType.Integer)
    private int goodsNumber;
}
