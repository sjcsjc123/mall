package api.auth;

import common.domian.Customer;

/**
 * 微信第三方登录
 */
public interface OauthWeChatService {

    Customer access();
}
