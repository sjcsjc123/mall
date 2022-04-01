package pay.service;

import api.pay.PayService;
import common.domian.TradePay;
import common.util.IDWorker;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import pay.mapper.PayMapper;

@DubboService
public class PayServiceImpl implements PayService {

    @Autowired
    private PayMapper payMapper;
    @Autowired
    private IDWorker idWorker;

    @Bean
    public IDWorker idWorker(){
        return new IDWorker(1,4);
    }

    @Override
    public void createPay(Long orderId,float payAmount) {
        TradePay pay = new TradePay();
        pay.setOrderId(orderId);
        pay.setPayId(idWorker.nextId());
        pay.setPayAmount(payAmount);
        pay.setIsPaid(1);
        payMapper.insert(pay);
    }

    @Override
    public void chancel(Long orderId) {
        payMapper.deleteById(orderId);
    }
}
