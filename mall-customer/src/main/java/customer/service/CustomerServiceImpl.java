package customer.service;

import api.customer.CustomerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.domian.Customer;
import common.domian.CustomerMoneyLog;
import customer.mapper.CustomerMapper;
import customer.mapper.CustomerMoneyLogMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DubboService
@Component
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerMoneyLogMapper customerMoneyLogMapper;

    @Override
    public Customer selectOne(Long id) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        return customerMapper.selectOne(wrapper);
    }

    @Override
    public void updateMoneyPaid(CustomerMoneyLog userMoneyLog) {
        customerMapper.updateCustomerMoney(userMoneyLog.getUseMoney(), userMoneyLog.getId());
        customerMoneyLogMapper.insert(userMoneyLog);
    }

}
