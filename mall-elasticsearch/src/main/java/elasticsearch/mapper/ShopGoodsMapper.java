package elasticsearch.mapper;

import elasticsearch.EsEntity.EsGoods;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopGoodsMapper {

    @Select("select shop_goods.goods_name,shop_goods.goods_id,shop_goods.goods_price," +
            "shop_goods.goods_desc,trade_goods.goods_number from shop_goods " +
            "left join trade_goods on trade_goods.goods_id = shop_goods" +
            ".goods_id")
    List<EsGoods> findAllById();

}
