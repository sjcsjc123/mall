package elasticsearch.controller;

import elasticsearch.service.EsGoodsService;
import elasticsearch.EsEntity.EsGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/es")
public class EsGoodsController {

    @Autowired
    private EsGoodsService esGoodsService;

    @RequestMapping("/import")
    public List<EsGoods> all(){
        esGoodsService.importAll();
        return esGoodsService.findAll();
    }

    /**
     * 价格升序
     * @param goodsName
     * @return
     */
    @PostMapping("/sort1")
    public List<EsGoods> sort1(@RequestParam("goodsName") String goodsName,
                               @RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize){
        return esGoodsService.findByKeywordAscPricePage(goodsName,pageNum,pageSize);
    }

    /**
     * 价格降序
     */
    @PostMapping("/sort2")
    public List<EsGoods> sort2(@RequestParam("goodsName") String goodsName,
                               @RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize){
        return esGoodsService.findByKeywordDescPricePage(goodsName,pageNum,
                pageSize);
    }

    /**
     * 上架时间
     */
    @PostMapping("/sort3")
    public List<EsGoods> sort3(@RequestParam("goodsName") String goodsName,
                               @RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize){
        return esGoodsService.findByKeywordDescIdPage(goodsName,pageNum,
                pageSize);
    }

    /**
     * 按销量排序
     */
    @PostMapping("/sort4")
    public List<EsGoods> sort4(@RequestParam("goodsName") String goodsName,
                               @RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize){
        return esGoodsService.findByKeywordDescGoodsNumber(goodsName,pageNum,
                pageSize);
    }
}
