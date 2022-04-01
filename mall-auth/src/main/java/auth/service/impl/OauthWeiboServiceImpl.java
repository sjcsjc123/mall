package auth.service.impl;

import api.auth.OauthWeiboService;
import common.domian.Customer;
import org.springframework.stereotype.Service;

@Service
public class OauthWeiboServiceImpl implements OauthWeiboService {
    @Override
    public Customer access(String code) {
        return null;
    }
}
