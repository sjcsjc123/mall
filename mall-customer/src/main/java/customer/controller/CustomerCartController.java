package customer.controller;

import api.customer.CustomerCartService;
import common.util.JwtTokenUtil;
import common.vo.CartIdVo;
import common.vo.CustomerCartItemVo;
import common.vo.CustomerCartVo;
import common.vo.TempUserVo;
import customer.interceptor.CartInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CustomerCartController {

    @Autowired
    private CustomerCartService customerCartService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/create")
    public CustomerCartItemVo create(@RequestParam("spuId") Long spuId,
                                     @RequestParam("skuId") Long skuId,
                                     @RequestParam("goods_number") int goodsNumber){
        TempUserVo tempUserVo = CartInterceptor.threadLocal.get();
        String userKey = null;
        if (tempUserVo.getId() == null){
            //未登录用户
            userKey = "cart:"+tempUserVo.getUserKey();
        }else {
            //登录用户
            userKey = "cart:"+tempUserVo.getId().toString();
        }
        return customerCartService.insert(userKey,skuId,spuId,
                goodsNumber);
    }

    @PostMapping("/delete")
    public String delete(@RequestBody List<CartIdVo> cartIdVos){
        TempUserVo tempUserVo = CartInterceptor.threadLocal.get();
        String userKey = null;
        if (tempUserVo.getId() == null){
            //未登录用户
            userKey = "cart:"+tempUserVo.getUserKey();
        }else {
            //登录用户
            userKey = "cart:"+tempUserVo.getId().toString();
        }
        customerCartService.delete(userKey, cartIdVos);
        return "删除购物车成功";
    }

    @PostMapping("/update")
    public String update(@RequestBody CartIdVo cartIdVo,
                         @RequestParam("goods_number") int goodsNumber){
        TempUserVo tempUserVo = CartInterceptor.threadLocal.get();
        String userKey = null;
        if (tempUserVo.getId() == null){
            //未登录用户
            userKey = "cart:"+tempUserVo.getUserKey();
        }else {
            //登录用户
            userKey = "cart:"+tempUserVo.getId().toString();
        }
        customerCartService.updateGoodsNumber(userKey,cartIdVo,goodsNumber);
        return "更改商品数量成功";
    }

    @GetMapping("/select")
    public CustomerCartVo select(){
        return customerCartService.select();
    }

    @PostMapping("/selectOne")
    public CustomerCartItemVo selectOne(@RequestBody CartIdVo cartIdVo){
        TempUserVo tempUserVo = CartInterceptor.threadLocal.get();
        String userKey = null;
        if (tempUserVo.getId() == null){
            //未登录用户
            userKey = "cart:"+tempUserVo.getUserKey();
        }else {
            //登录用户
            userKey = "cart:"+tempUserVo.getId().toString();
        }
        return customerCartService.selectOne(cartIdVo,userKey);
    }

    /**
     * 将从请求中获取用户名重复的代码挤出来
     */
    public String getUsernameFromRequest(HttpServletRequest request){
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
