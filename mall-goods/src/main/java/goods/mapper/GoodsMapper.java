package goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import common.domian.TradeGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

public interface GoodsMapper extends BaseMapper<TradeGoods> {

    @Update("update from trade_goods set goods_number = goods_number - " +
            "#{goods_number} where goods_id = #{goods_id}")
    void reduceGoodsNumber(@Param("goods_number") int goods_number,
                           @Param("goods_id") Long goods_id);

}
