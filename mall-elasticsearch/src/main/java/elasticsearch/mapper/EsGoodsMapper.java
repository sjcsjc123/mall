package elasticsearch.mapper;

import elasticsearch.EsEntity.EsGoods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsGoodsMapper extends ElasticsearchRepository<EsGoods,Long> {
}
