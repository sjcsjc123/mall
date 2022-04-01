package business.controller;

import api.business.BusinessShopService;
import api.goods.ShopGoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.BusinessShop;
import common.domian.ShopGoods;
import common.exception.MyProjectException;
import common.util.JwtTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shopGoods")
public class ShopGoodsController {

    @DubboReference
    private ShopGoodsService shopGoodsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private BusinessShopService businessShopService;

    @PostMapping("/insert")
    public String insert(@RequestParam("goods_name") String goodsName,
                         @RequestParam("shop_id") Long shopId,
                         @RequestParam("goods_price") float goodsPrice,
                         @RequestParam("goods_desc") String goodsDesc,
                         @RequestParam("goods_number") int goodsNumber,
                         HttpServletRequest request){
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (StringUtils.isEmpty(goodsDesc)){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_DESCRIPTION_NULL);
        }
        if (StringUtils.isEmpty(goodsName)){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_NAME_NULL);
        }
        if (goodsNumber <= 0){
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_NUMBER_NULL);
        }
        if (goodsPrice <= 0){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_MONEY_NULL);
        }
        BusinessShop businessShop = businessShopService.select(shopId);
        if (businessShop == null){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_NULL);
        }
        ShopGoods shopGoods = new ShopGoods();
        shopGoods.setShopId(shopId);
        shopGoods.setGoodsDesc(goodsDesc);
        shopGoods.setGoodsName(goodsName);
        shopGoods.setGoodsSold(0);
        shopGoods.setGoodsPrice(goodsPrice);
        shopGoods.setGoodsNumber(goodsNumber);
        shopGoods.setBusinessName(username);
        shopGoodsService.insert(shopGoods);
        return "添加成功";
    }

    @PostMapping("/update")
    public String update(@RequestParam("goods_name") String goodsName,
                         @RequestParam("goods_price") float goodsPrice,
                         @RequestParam("goods_desc") String goodsDesc,
                         @RequestParam("goods_number") int goodsNumber,
                         @RequestParam("goods_id") Long goodsId){
        if (StringUtils.isEmpty(goodsDesc)){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_DESCRIPTION_NULL);
        }
        if (StringUtils.isEmpty(goodsName)){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_NAME_NULL);
        }
        if (goodsNumber <= 0){
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_NUMBER_NULL);
        }
        if (goodsPrice <= 0){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_MONEY_NULL);
        }
        ShopGoods shopGoods = shopGoodsService.select(goodsId);
        if (shopGoods == null){
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_NULL);
        }
        shopGoods.setGoodsDesc(goodsDesc);
        shopGoods.setGoodsName(goodsName);
        shopGoods.setGoodsNumber(goodsNumber);
        shopGoods.setGoodsPrice(goodsPrice);
        shopGoodsService.update(shopGoods);
        return "更改成功";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("goods_id") Long goodsId){
        ShopGoods shopGoods = shopGoodsService.select(goodsId);
        if (shopGoods == null){
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_NULL);
        }
        shopGoodsService.delete(goodsId);
        return "商品删除成功";
    }

    @PostMapping("/select")
    public IPage<ShopGoods> selectByShopId(@RequestParam("shop_id") Long shopId){
        return shopGoodsService.selectByShopId(shopId);
    }
}
