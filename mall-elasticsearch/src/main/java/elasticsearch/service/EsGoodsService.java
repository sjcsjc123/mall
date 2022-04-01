package elasticsearch.service;


import elasticsearch.EsEntity.EsGoods;

import java.util.List;

/**
 * 整合elastic search
 */
public interface EsGoodsService {

    /**
     * 查询所有
     */
    List<EsGoods> findAll();

    /**
     * 从数据库导入商品数据
     */
    void importAll();

    /**
     * 价格升序
     */
    List<EsGoods> findByKeywordAscPricePage(String keyword, int pageNum,
                                            int pageSize);

    /**
     * 价格降序
     */
    List<EsGoods> findByKeywordDescPricePage(String keyword,int pageNum,
                                            int pageSize);

    /**
     * 按最新时间排序
     */
    List<EsGoods> findByKeywordDescIdPage(String keyword,int pageNum,
                                            int pageSize);

    /**
     * 按销量排序
     */
    List<EsGoods> findByKeywordDescGoodsNumber(String keyword,int pageNum,
                                               int pageSize);

}
