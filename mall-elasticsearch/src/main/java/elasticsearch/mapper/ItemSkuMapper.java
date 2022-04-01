package elasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import common.domian.ItemSkuInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSkuMapper extends BaseMapper<ItemSkuInfo> {

    @Select("select sku_price from item_sku_info where sku_id = #{sku_id} and" +
            " spu_id = #{spu_id}")
    float selectSkuPrice(@Param("sku_id") Long skuId,
                         @Param("spu_id") Long spuId);
}
