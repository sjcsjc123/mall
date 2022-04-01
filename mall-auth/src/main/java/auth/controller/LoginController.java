package auth.controller;

import api.auth.LoginService;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.Customer;
import common.exception.MyProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/customer")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestParam("customer_name") String username,
                        @RequestParam("customer_password") String password,
                        HttpSession session){
        loginService.login(username, password);
        session.setAttribute("loginUser",username);
        return "登录成功";
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("loginUser") != null) {
            // 用户已登录
            return "redirect:http://customer.mall.com/test/test1";
        }
        return "login";
    }

    @ResponseBody
    @PostMapping("/register")
    public String register(@RequestParam("customer_name") String username,
                           @RequestParam("customer_phone") String phone,
                           @RequestParam("customer_password") String password,
                           @RequestParam("confirm") String confirm){
        if (!confirm.equals(password)){
            throw new MyProjectException(MyProjectExceptionEnum.PASSWORD_NOT_CONFIRM);
        }
        Customer customer = new Customer();
        customer.setCustomerName(username);
        customer.setCustomerPhone(phone);
        customer.setCustomerPassword(password);
        loginService.register(customer);
        return "注册成功";
    }
}
