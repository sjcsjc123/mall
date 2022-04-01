package elasticsearch.controller;

import elasticsearch.service.ItemService;
import common.vo.ItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用于展示单个商品的信息
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/{spuId}")
    public List<ItemVo> showBySpuId(@PathVariable("spuId") Long spuId){
        return itemService.showBySpuId(spuId);
    }

    @GetMapping("/{spuId}/{skuId}")
    public ItemVo showBySpuIdAndSkuId(@PathVariable("spuId") Long spuId,
                                      @PathVariable("skuId") Long skuId){
        return itemService.showBySpuIdAndSkuId(spuId,skuId);
    }
}
