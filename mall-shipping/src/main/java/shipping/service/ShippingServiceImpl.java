package shipping.service;

import api.shipping.ShippingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.util.IDWorker;
import common.domian.TradeShipping;
import org.apache.dubbo.config.annotation.DubboService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import shipping.mapper.ShippingMapper;

@DubboService
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private IDWorker idWorker;

    @Bean
    public IDWorker idWorker(){
        return new IDWorker(1,5);
    }

    @Override
    public Long createShipping(Long orderId) {
        TradeShipping shipping = new TradeShipping();
        shipping.setOrderId(orderId);
        shipping.setShippingStatus(1);
        shipping.setShippingId(idWorker.nextId());
        shippingMapper.insert(shipping);
        return shipping.getShippingId();
    }

    @Override
    public void updateShippingStatus(Long orderId,int shippingStatus) {
        QueryWrapper<TradeShipping> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderId);
        TradeShipping shipping = shippingMapper.selectOne(wrapper);
        shipping.setShippingStatus(shippingStatus);
        shippingMapper.update(shipping,wrapper);
    }

    @Override
    public void delete(Long orderId) {
        shippingMapper.deleteById(orderId);
    }
}
