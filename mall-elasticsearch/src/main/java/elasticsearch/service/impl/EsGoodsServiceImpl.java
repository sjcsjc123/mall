package elasticsearch.service.impl;

import elasticsearch.EsEntity.EsGoods;
import elasticsearch.mapper.*;
import elasticsearch.service.EsGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@DubboService
@Component
@Slf4j
public class EsGoodsServiceImpl implements EsGoodsService {

    @Autowired
    private EsGoodsMapper esGoodsMapper;
    @Autowired
    private ShopGoodsMapper shopGoodsMapper;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    /**
     * 查找所有
     * @return
     */
    @Override
    public List<EsGoods> findAll() {
        Iterable<EsGoods> goods = esGoodsMapper.findAll();
        Iterator<EsGoods> iterator = goods.iterator();
        List<EsGoods> esGoods = new ArrayList<>();
        while (iterator.hasNext()){
            esGoods.add(iterator.next());
        }
        return esGoods;
    }

    /**
     * 从数据库导入
     */
    @Override
    public void importAll() {
        List<EsGoods> esGoods = shopGoodsMapper.findAllById();
        esGoodsMapper.saveAll(esGoods);
    }

    /**
     * 价格升序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<EsGoods> findByKeywordAscPricePage(String keyword,
                                                   int pageNum, int pageSize) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
                "goodsName",keyword);
        SortBuilder sortBuilder =
                SortBuilders.fieldSort("goodsPrice").order(SortOrder.ASC);
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withFilter(matchQueryBuilder)
                .withSort(sortBuilder)
                .withPageable(pageable)
                .build();
        return sendRequest(nativeSearchQuery);
    }

    /**
     * 价格降序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<EsGoods> findByKeywordDescPricePage(String keyword,
                                                    int pageNum, int pageSize) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
                "goodsName",keyword);
        SortBuilder sortBuilder =
                SortBuilders.fieldSort("goodsPrice").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withFilter(matchQueryBuilder)
                .withSort(sortBuilder)
                .withPageable(pageable)
                .build();
        return sendRequest(nativeSearchQuery);
    }

    /**
     * 按更新时间排序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<EsGoods> findByKeywordDescIdPage(String keyword,
                                                   int pageNum, int pageSize) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
                "goodsName",keyword);
        SortBuilder sortBuilder =
                SortBuilders.fieldSort("goodsId").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withFilter(matchQueryBuilder)
                .withSort(sortBuilder)
                .withPageable(pageable)
                .build();
        return sendRequest(nativeSearchQuery);
    }

    /**
     * 按照销量排序
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<EsGoods> findByKeywordDescGoodsNumber(String keyword,
                                                      int pageNum,
                                                      int pageSize) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(
                "goodsName",keyword);
        SortBuilder sortBuilder =
                SortBuilders.fieldSort("goodsNumber").order(SortOrder.DESC);
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withFilter(matchQueryBuilder)
                .withSort(sortBuilder)
                .withPageable(pageable)
                .build();
        return sendRequest(nativeSearchQuery);
    }

    /**
     * 将处理请求返回list列表挤出来
     */
    public List<EsGoods> sendRequest(NativeSearchQuery nativeSearchQuery){
        List<SearchHit<EsGoods>> searchHits =
                restTemplate.search(nativeSearchQuery, EsGoods.class).getSearchHits();
        List<EsGoods> list = new ArrayList<>();
        for (SearchHit<EsGoods> searchHit : searchHits) {
            EsGoods esGoods = searchHit.getContent();
            list.add(esGoods);
        }
        return list;
    }

}
