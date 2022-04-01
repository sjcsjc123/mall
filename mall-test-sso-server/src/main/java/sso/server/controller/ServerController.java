package sso.server.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Scanner;

/**
 * 模拟单点登录处处登录
 * 思路：其它系统需要登录时就跳转到登录认证系统，并且携带request_url来实现登录成功就返回原来
 * 系统的index页，登录成功则传递session过去实现全系统session共享
 */
@RestController
public class ServerController {

    @GetMapping("/login")
    public String login(){
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        if (username == null){
            return "登录失败";
        }
        return "登录成功";
    }
}
