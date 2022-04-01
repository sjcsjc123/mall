package api.auth;

import common.domian.Customer;

public interface LoginService {

    /**
     * 登录
     * @param username
     * @param password
     */
    void login(String username,String password);

    /**
     * 注册
     */
    void register(Customer customer);

    /**
     * 退出
     * 将在线状态设置为0即可
     */

    /**
     * 注销
     * 将表格中对应的id删除以及删除header中的token即可
     */
}
