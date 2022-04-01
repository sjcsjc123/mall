package sso.client2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class Client2Controller {

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "你好，我是客户端2";
    }

    @PostMapping("/nologin")
    public String noLogin(){
        return "redirect:http://server.com:8000/login?"+
                "request_url=http://client2.com:8002";
    }
}
