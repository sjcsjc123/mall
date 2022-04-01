package order.controller;

import api.order.OrderService;
import common.domian.TradeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public String create(@RequestParam("id") Long id,
                         @RequestParam("address") String address,
                         @RequestParam("consignee") String consignee,
                         @RequestParam("goods_id") Long goodsId,
                         @RequestParam("goods_number") int goodsNumber,
                         @RequestParam("coupon_id") Long couponId,
                         @RequestParam("money_status") int moneyStatus){
        TradeOrder order = new TradeOrder();
        order.setId(id);
        order.setAddress(address);
        order.setConsignee(consignee);
        order.setGoodsId(goodsId);
        order.setGoodsNumber(goodsNumber);
        order.setCouponId(couponId);
        order.setMoneyStatus(moneyStatus);
        orderService.insert(order);
        return "创建订单成功";
    }
}
