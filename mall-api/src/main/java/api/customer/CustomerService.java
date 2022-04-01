package api.customer;

import common.domian.Customer;
import common.domian.CustomerMoneyLog;

/**
 * 用于处理消费者的登录、注册、注销、退出等一系列操作
 */
public interface CustomerService {
    /**
     * 查找用户是否存在
     */
    Customer selectOne(Long id);

    void updateMoneyPaid(CustomerMoneyLog userMoneyLog);
}
