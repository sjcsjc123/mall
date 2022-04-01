package customer.service;

import api.customer.CustomerCartService;
import api.goods.ShopGoodsService;
import com.alibaba.fastjson.JSON;
import common.domian.ItemSkuSaleInfo;
import common.redisUtils.RedisService;
import common.vo.*;
import common.util.IDWorker;
import customer.interceptor.CartInterceptor;
import customer.mapper.CustomerMapper;
import elasticsearch.service.ItemService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
@Component
public class CustomerCartServiceImpl implements CustomerCartService {

    @Autowired
    private CustomerMapper customerMapper;
    @DubboReference
    private ShopGoodsService shopGoodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private IDWorker idWorker;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @DubboReference
    private ItemService itemService;

    /**
     * 注意，用postman测试时记得带上cookie，否则无法检测出redis存在的情况
     * @param userKey
     * @param skuId
     * @param spuId
     * @param goodsNumber
     * @return
     */
    @Override
    public CustomerCartItemVo insert(String userKey, Long skuId,
                                     Long spuId,int goodsNumber) {
        String redisKey = "spuId:"+spuId.toString()+
                "skuId:"+skuId.toString();
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(userKey);
        String oldCartItem =
                ops.get(redisKey);
        if (oldCartItem != null){
            //购物车里有相同的商品，则改变商品数量
            CustomerCartItemVo customerCartItemVo =
                    JSON.parseObject(oldCartItem, CustomerCartItemVo.class);
            customerCartItemVo.setGoodsNumber(goodsNumber+customerCartItemVo.getGoodsNumber());
            customerCartItemVo.setGoodsAmount(customerCartItemVo.getGoodsAmount()
                    +goodsNumber*customerCartItemVo.getSkuPrice());
            ops.put(redisKey,JSON.toJSONString(customerCartItemVo));
            return customerCartItemVo;
        }
        //如果购物车里没有该商品，则新建一个
        /**
         * 此处可使用异步编排线程，来缩小响应时间
         */
        CustomerCartItemVo customerCartItemVo = new CustomerCartItemVo();
        customerCartItemVo.setSkuId(skuId);
        customerCartItemVo.setSpuId(spuId);
        customerCartItemVo.setGoodsNumber(goodsNumber);
        ItemVo itemVo = itemService.showBySpuIdAndSkuId(spuId, skuId);
        customerCartItemVo.setGoodsName(itemVo.getSpuName());
        customerCartItemVo.setSkuPrice(itemVo.getSkuPrice());
        customerCartItemVo.setGoodsAmount(customerCartItemVo.getSkuPrice()*goodsNumber);
        List<ItemSkuSaleInfo> skuSaleInfos = itemVo.getSkuSaleInfos();
        List<ItemAttrVo> itemAttrVos = new ArrayList<>();
        for (ItemSkuSaleInfo skuSaleInfo : skuSaleInfos) {
            if (skuSaleInfo.getSkuId() == skuId && skuSaleInfo.getSpuId() == spuId){
                ItemAttrVo itemAttrVo = new ItemAttrVo();
                itemAttrVo.setAttrId(skuSaleInfo.getAttrId());
                itemAttrVo.setAttrName(skuSaleInfo.getAttrName());
                itemAttrVo.setAttrValue(skuSaleInfo.getAttrValue());
                itemAttrVos.add(itemAttrVo);
            }
        }
        customerCartItemVo.setItemAttrVos(itemAttrVos);
        ops.put(redisKey,JSON.toJSONString(customerCartItemVo));
        return customerCartItemVo;
    }

    @Override
    public void delete(String userKey, List<CartIdVo> cartIdVos) {
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(userKey);
        for (CartIdVo cartIdVo : cartIdVos) {
            String redisKey = "spuId:"+cartIdVo.getSpuId().toString()+
                    "skuId:"+cartIdVo.getSkuId().toString();
            ops.delete(redisKey);
        }
    }

    @Override
    public void updateGoodsNumber(String userKey, CartIdVo cartIdVo, int goodsNumber) {
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(userKey);
        String redisKey = "spuId:"+cartIdVo.getSpuId().toString()+
                        "skuId:"+cartIdVo.getSkuId().toString();
        List<CustomerCartItemVo> customerCartItemVos =
                ops.values().stream().map(value -> JSON.parseObject(value,
                CustomerCartItemVo.class)).collect(Collectors.toList());
        for (CustomerCartItemVo customerCartItemVo : customerCartItemVos) {
            if (customerCartItemVo.getSpuId().equals(cartIdVo.getSpuId()) &&
            customerCartItemVo.getSkuId().equals(cartIdVo.getSkuId())){
                customerCartItemVo.setGoodsNumber(goodsNumber);
                customerCartItemVo.setGoodsAmount(goodsNumber*customerCartItemVo.getSkuPrice());
                ops.put(redisKey,JSON.toJSONString(customerCartItemVo));
            }
        }
    }

    @Override
    public CustomerCartVo select() {
        CustomerCartVo customerCartVo = new CustomerCartVo();
        TempUserVo tempUserVo = CartInterceptor.threadLocal.get();
        //由于user-key会持续一个月，可以先获取未登录购物车记录
        String userKey = "cart:"+tempUserVo.getUserKey();
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(userKey);
        List<String> values = ops.values();
        List<CustomerCartItemVo> tempCarts =
                values.stream().map(value -> JSON.parseObject(value,
                CustomerCartItemVo.class)).collect(Collectors.toList());
        //获取到临时购物车之后判断用户是否登录
        if (tempUserVo.getId() == null){
            //未登录
            customerCartVo.setUserKey(tempUserVo.getUserKey());
            customerCartVo.setCustomerCartItemVos(tempCarts);
        }else {
            //已登陆，合并购物车
            String userKey1 ="cart:"+tempUserVo.getId().toString();
            if (!CollectionUtils.isEmpty(tempCarts)){
                tempCarts.forEach(tempCart -> moveToCertainCart(tempCart,userKey1));
                //清空临时购物车
                redisTemplate.delete(userKey);
            }
            BoundHashOperations<String, String, String> ops1 =
                    redisTemplate.boundHashOps(userKey1);
            List<CustomerCartItemVo> cartItemVos = ops1.values().stream().map(
                    value -> JSON.parseObject(value, CustomerCartItemVo.class)
            ).collect(Collectors.toList());
            customerCartVo.setCustomerCartItemVos(cartItemVos);
            customerCartVo.setUserKey(tempUserVo.getId().toString());
        }
        return customerCartVo;

    }

    @Override
    public CustomerCartItemVo selectOne(CartIdVo cartIdVo, String userKey) {
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(userKey);
        List<CustomerCartItemVo> cartItemVos = ops.values().stream().map(
                value -> JSON.parseObject(value, CustomerCartItemVo.class)
        ).collect(Collectors.toList());
        for (CustomerCartItemVo cartItemVo : cartItemVos) {
            if (cartItemVo.getSkuId() == cartIdVo.getSkuId()
                    && cartItemVo.getSpuId() == cartIdVo.getSkuId()){
                return cartItemVo;
            }
        }
        return null;
    }

    /**
     * 将商品合并到指定的购物车中
     *
     * 主要用于 合并临时购物车到正式购物车
     * @return
     */
    private void moveToCertainCart(CustomerCartItemVo itemVO, String cartKey) {
        // 判断购物车中该商品是否存在
        // 如果购物车中没有这个商品，那就是新加；如果有，那就是修改数量
        String redisKey = "spuId:"+itemVO.getSpuId().toString()+
                "skuId:"+itemVO.getSkuId().toString();
        BoundHashOperations<String, String, String> ops =
                redisTemplate.boundHashOps(cartKey);
        String orderCart = ops.get(redisKey);
        // 有
        if (orderCart != null) {
            CustomerCartItemVo cartItem = JSON.parseObject(orderCart, CustomerCartItemVo.class);
            cartItem.setGoodsAmount(cartItem.getGoodsAmount() + itemVO.getGoodsAmount());
            cartItem.setGoodsNumber(cartItem.getGoodsNumber() + itemVO.getGoodsNumber());
            ops.put(redisKey, JSON.toJSONString(cartItem));
        } else {
            // 没有，直接挪进去
            ops.put(redisKey, JSON.toJSONString(itemVO));
        }
    }
}
