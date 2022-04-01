package business.controller;

import api.business.BusinessService;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.Business;
import common.exception.MyProjectException;
import common.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/business")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @PostMapping("/login")
    public String login(@RequestParam("business_name") String username,
                        @RequestParam("business_password") String password,
                        HttpServletResponse response){
        String token = businessService.login(username, password);
        response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY,token);
        return "登录成功";
    }

    @PostMapping("/register")
    public String register(@RequestParam("business_name") String username,
                           @RequestParam("business_phone") String phone,
                           @RequestParam("business_password") String password,
                           @RequestParam("confirm") String confirm){
        if (!confirm.equals(password)){
            throw new MyProjectException(MyProjectExceptionEnum.PASSWORD_NOT_CONFIRM);
        }
        Business business = new Business();
        business.setBusinessName(username);
        business.setBusinessPhone(phone);
        business.setBusinessPassword(password);
        businessService.register(business);
        return "注册成功";
    }
}
