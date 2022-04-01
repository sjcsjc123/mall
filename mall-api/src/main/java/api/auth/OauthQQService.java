package api.auth;

import common.domian.Customer;

/**
 * QQ第三方登录
 */
public interface OauthQQService {

    Customer access();
}
