package elasticsearch.service;

import common.vo.ItemVo;

import java.util.List;

/**
 * 用于展示商品详细信息
 */
public interface ItemService {

    /**
     * 获取商品所有属性
     * @param spuId
     * @return
     */
    List<ItemVo> showBySpuId(Long spuId);

    /**
     * 获取商品单个属性，与购物车对接
     */
    ItemVo showBySpuIdAndSkuId(Long spuId,Long skuId);
}
