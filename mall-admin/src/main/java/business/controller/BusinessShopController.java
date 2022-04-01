package business.controller;

import api.business.BusinessShopService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.BusinessShop;
import common.exception.MyProjectException;
import common.util.JwtTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/shop")
public class BusinessShopController {

    @Autowired
    private BusinessShopService businessShopService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/insert")
    public String insert(HttpServletRequest request,
                         @RequestParam("shop_name") String shopName){
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (StringUtils.isEmpty(shopName)){
            throw new MyProjectException(MyProjectExceptionEnum.BUSINESS_NAME_NULL);
        }
        BusinessShop businessShop = new BusinessShop();
        businessShop.setBusinessName(username);
        businessShop.setShopName(shopName);
        businessShopService.insert(businessShop);
        return "上架店铺成功";
    }

    @PostMapping("/update")
    public String update(@RequestParam("shop_name") String shopName,
                         @RequestParam("shop_id") Long shopId){
        if (StringUtils.isEmpty(shopName)){
            throw new MyProjectException(MyProjectExceptionEnum.BUSINESS_NAME_NULL);
        }
        BusinessShop businessShop = businessShopService.select(shopId);
        if (businessShop == null){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_NULL);
        }
        businessShop.setShopName(shopName);
        businessShopService.update(businessShop);
        return "修改店铺信息成功";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("shop_id") Long shopId){
        BusinessShop businessShop = businessShopService.select(shopId);
        if (businessShop == null){
            throw new MyProjectException(MyProjectExceptionEnum.SHOP_NULL);
        }
        businessShopService.delete(shopId);
        return "下架商品成功";
    }

    @GetMapping("/select")
    public IPage<BusinessShop> selectAll(HttpServletRequest request){
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return businessShopService.selectAllByBusinessName(username);
    }
}
