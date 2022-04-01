package api.auth;

import common.domian.Customer;

/**
 * 微博第三方登录
 */
public interface OauthWeiboService {

    Customer access(String code);
}
