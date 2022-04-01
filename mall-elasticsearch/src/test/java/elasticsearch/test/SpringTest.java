package elasticsearch.test;

import elasticsearch.EsEntity.EsGoods;
import elasticsearch.mapper.EsGoodsMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringTest {

    @Autowired
    private EsGoodsMapper esGoodsMapper;

    @Test
    public void test(){
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("goodsName",
                "美的");
        Iterable<EsGoods> search = esGoodsMapper.search(queryBuilder);
        for (EsGoods esGoods : search) {
            System.out.println("=================");
            System.out.println(esGoods);

        }
    }
}
