package order.service;

import api.coupon.CouponService;
import api.customer.CustomerService;
import api.goods.GoodsService;
import api.order.OrderService;
import api.pay.PayService;
import api.shipping.ShippingService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.*;
import common.exception.MyProjectException;
import common.util.IDWorker;
import lombok.extern.slf4j.Slf4j;
import order.mapper.TradeOrderMapper;
import common.mqEntity.OrderMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@DubboService
@Component
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final static String TOPIC = "chancelNoPaidTopic";
    private final static String TAG = "chancelNoPaidTag";

    @Autowired
    private TradeOrderMapper tradeOrderMapper;
    @Autowired
    private IDWorker idWorker;
    @DubboReference
    private GoodsService goodsService;
    @DubboReference
    private CustomerService customerService;
    @DubboReference
    private CouponService couponService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @DubboReference
    private PayService payService;
    @DubboReference
    private ShippingService shippingService;

    /**
     * 创建订单
     * @param order
     */
    @Override
    public void insert(TradeOrder order) {
        Customer customer = customerService.selectOne(order.getId());
        TradeGoods tradeGoods = goodsService.selectOne(order.getGoodsId());
        //1.校验订单
        checkOrder(order,customer,tradeGoods);
        //2.生成预订单
        savePreOrder(order,customer);
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(order.getOrderId());
        orderMessage.setCouponId(order.getCouponId());
        orderMessage.setGoodsId(order.getGoodsId());
        orderMessage.setId(order.getId());
        orderMessage.setMoneyPaid(order.getMoneyPaid());
        orderMessage.setGoodsNumber(order.getGoodsNumber());
        try {
            //3.扣减库存
            reduceGoodsNum(order);
            //4.扣减优惠券
            updateCouponStatus(order);
            //5.使用余额
            reduceMoneyPaid(order);
            //6.确认订单
            updateOrderStatus(order);
            //7.一个小时未支付发送取消订单的消息
            sendChancelNoPaidDelayMessage(TOPIC,TAG,order.getOrderId().toString(),
                    JSON.toJSONString(orderMessage));
        } catch (Exception e) {
            try {
                sendChancelNoPaidMessage(TOPIC,TAG,order.getOrderId().toString(),
                        JSON.toJSONString(orderMessage));
            } catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }

    private void sendChancelNoPaidMessage(String topic, String tag,
                                          String orderId, String body) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(topic,tag,orderId,
                body.getBytes(StandardCharsets.UTF_8));
        rocketMQTemplate.getProducer().send(message);
    }

    /**
     * 发送用户未支付取消未支付订单延迟消息
     */
    private void sendChancelNoPaidDelayMessage(String topic, String tag,
                                          String orderId, String body) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(topic,tag,orderId,
                body.getBytes(StandardCharsets.UTF_8));
        //messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m
        // 30m 1h 2h 7d
        message.setDelayTimeLevel(17);
        rocketMQTemplate.getProducer().send(message);
    }

    /**
     * 确认订单
     * @param order
     */
    private void updateOrderStatus(TradeOrder order) {
        order.setOrderStatus(1);
        order.setPayStatus(0);
        order.setConfirmTime(new Date());
        QueryWrapper<TradeOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",order.getOrderId());
        tradeOrderMapper.update(order,wrapper);
        log.info("订单:"+order.getOrderId()+"确认订单成功");
    }

    /**
     * 使用余额
     * @param order
     */
    private void reduceMoneyPaid(TradeOrder order) {
        if(order.getMoneyPaid() >= 0){
            CustomerMoneyLog userMoneyLog = new CustomerMoneyLog();
            userMoneyLog.setOrderId(order.getOrderId());
            userMoneyLog.setId(order.getId());
            userMoneyLog.setUseMoney(order.getMoneyPaid());
            userMoneyLog.setMoneyLogType(1);
            customerService.updateMoneyPaid(userMoneyLog);
            log.info("订单:"+order.getOrderId()+",扣减余额成功");
        }
    }

    /**
     * 扣减优惠券
     * @param order
     */
    private void updateCouponStatus(TradeOrder order) {
        if(order.getCouponId()!=null){
            TradeCoupon coupon = couponService.selectOne(order.getCouponId());
            coupon.setOrderId(order.getOrderId());
            coupon.setIsUsed(1);
            coupon.setUsedTime(new Date());
            //更新优惠券状态
            couponService.updateCouponStatus(coupon);
            log.info("订单:"+order.getOrderId()+",使用优惠券");
        }
    }

    /**
     * 扣减库存
     * @param order
     */
    private void reduceGoodsNum(TradeOrder order) {
        TradeGoodsNumberLog goodsNumberLog = new TradeGoodsNumberLog();
        goodsNumberLog.setOrderId(order.getOrderId());
        goodsNumberLog.setGoodsId(order.getGoodsId());
        goodsNumberLog.setGoodsNumber(order.getGoodsNumber());
        goodsService.reduceGoodsNumber(goodsNumberLog);
        log.info("订单:"+order.getOrderId()+"扣减库存成功");
    }

    /**
     * 生成预订单
     * @param order
     * @return
     */
    private void savePreOrder(TradeOrder order,Customer customer) {
        order.setOrderStatus(0);//订单未确认
        order.setOrderId(idWorker.nextId());
        float shippingFee = calculateShippingFee(order.getOrderAmount());
        order.setShippingFee(shippingFee);
        //未计算运费的价格
        float orderAmountBefore = order.getGoodsPrice()*order.getGoodsNumber();
        //计算运费后的订单价格
        float orderAmount = orderAmountBefore + shippingFee;
        order.setOrderAmount(orderAmount);
        //使用优惠券,couponId存在则表示使用优惠券
        order.setCouponPaid(0);
        if (order.getCouponId() != null){
            TradeCoupon coupon = couponService.selectOne(order.getCouponId());
            if (coupon.getExpireTime().before(new Date()) ||
                    coupon.getIsUsed() == 1){
                throw new MyProjectException(MyProjectExceptionEnum.COUPON_NULL);
            }
            order.setCouponPaid(coupon.getCouponPrice());
        }
        //设置使用余额之前需要支付的总价格
        float payAmountBefore = orderAmount - order.getCouponPaid();
        //不使用余额
        if (order.getMoneyStatus() == 0){
            order.setMoneyPaid(0);
            order.setPayAmount(payAmountBefore);
        }else {
            //使用余额
            if (customer.getCustomerMoney() <= 0){
                throw new MyProjectException(MyProjectExceptionEnum.MONEY_PAID_INVALID);
            }
            //余额大于订单价格
            if(customer.getCustomerMoney() >= payAmountBefore){
                order.setMoneyPaid(payAmountBefore);
                order.setPayAmount(0);
            }else {
                order.setMoneyPaid(customer.getCustomerMoney());
                order.setPayAmount(payAmountBefore - customer.getCustomerMoney());
            }
            order.setAddTime(new Date());
            tradeOrderMapper.insert(order);
        }
    }

    /**
     * 核算运费
     * @param orderAmount
     * @return
     */
    private float calculateShippingFee(float orderAmount) {
        if(orderAmount >= 100){
            return 0;
        }else{
            return 10f;
        }
    }

    /**
     * 校验订单
     * @param order
     */
    private void checkOrder(TradeOrder order,Customer customer,TradeGoods tradeGoods) {
        if (order == null){
            throw new MyProjectException(MyProjectExceptionEnum.ORDER_NULL);
        }
        if (tradeGoods == null){
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_NULL);
        }
        if (customer == null){
            throw new MyProjectException(MyProjectExceptionEnum.USERNAME_IS_EMPTY);
        }
        if (order.getGoodsPrice() != tradeGoods.getGoodsPrice()){
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_PRICE_INVALID);
        }
        if (order.getGoodsNumber() >= tradeGoods.getGoodsNumber()){
            throw new MyProjectException(MyProjectExceptionEnum.GOODS_NUMBER_LESS);
        }
        order.setGoodsPrice(tradeGoods.getGoodsPrice());
        log.info("校验订单通过");
    }

    /**
     * 取消未支付订单
     * @param
     */
    @Override
    public void chancelNoPaidOrder(OrderMessage orderMessage) {
        //回退库存
        goodsService.chancelReduce(orderMessage.getGoodsId(),orderMessage.getGoodsNumber());
        //回退余额
        CustomerMoneyLog customerMoneyLog = new CustomerMoneyLog();
        customerMoneyLog.setMoneyLogType(2);
        customerMoneyLog.setUseMoney(-orderMessage.getMoneyPaid());
        customerMoneyLog.setOrderId(orderMessage.getOrderId());
        customerMoneyLog.setId(orderMessage.getId());
        customerMoneyLog.setCreateTime(new Date());
        customerService.updateMoneyPaid(customerMoneyLog);
        //更改订单信息
        tradeOrderMapper.chancelNoPaid(orderMessage.getOrderId());
    }

    /**
     * 取消已支付订单
     * @param
     */
    @Override
    public void chancelPaidOrder(OrderMessage orderMessage) {
        //回退库存
        goodsService.chancelReduce(orderMessage.getGoodsId(),orderMessage.getGoodsNumber());
        //回退余额
        CustomerMoneyLog customerMoneyLog = new CustomerMoneyLog();
        customerMoneyLog.setMoneyLogType(2);
        customerMoneyLog.setUseMoney(-orderMessage.getMoneyPaid());
        customerMoneyLog.setOrderId(orderMessage.getOrderId());
        customerMoneyLog.setId(orderMessage.getId());
        customerMoneyLog.setCreateTime(new Date());
        customerService.updateMoneyPaid(customerMoneyLog);
        //更改订单信息
        tradeOrderMapper.chancelPaid(orderMessage.getOrderId());
        //删除支付订单信息
        payService.chancel(orderMessage.getOrderId());
        //删除快递单号信息
        shippingService.delete(orderMessage.getOrderId());
    }

    @Override
    public void payOrder(Long orderId){
        //更新订单状态
        QueryWrapper<TradeOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderId);
        TradeOrder tradeOrder = tradeOrderMapper.selectOne(wrapper);
        tradeOrder.setPayStatus(1);
        tradeOrder.setShippingFee(0);
        tradeOrder.setPayTime(new Date());
        //创建快递单号
        Long shippingId = shippingService.createShipping(orderId);
        tradeOrder.setShippingId(shippingId);
        tradeOrderMapper.update(tradeOrder,wrapper);
        //创建支付订单
        //之前扣减余额是锁定余额，此处才是真正扣减了余额，故不需要对余额进行操作，取消订单退回余额即可
        payService.createPay(orderId,tradeOrder.getPayAmount());
    }
}
