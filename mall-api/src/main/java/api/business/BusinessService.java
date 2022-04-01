package api.business;

import common.domian.Business;

/**
 * 用于处理商家登录、注册、注销、退出等一系列操作
 */
public interface BusinessService {

    /**
     * 登录
     */
    String login(String username,String password);

    /**
     * 注册
     */
    void register(Business business);

    /**
     * 注销和退出同消费者
     */
}
