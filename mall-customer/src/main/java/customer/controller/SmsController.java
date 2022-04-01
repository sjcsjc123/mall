package customer.controller;

import common.exception.constant.MyProjectExceptionEnum;
import common.exception.MyProjectException;
import common.redisUtils.RedisService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import third.service.SmsService;

import java.util.UUID;

/**
 * 发送短信测试
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @DubboReference
    private SmsService smsService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/send")
    public String send(@RequestParam("phone") String phone){
        if (redisService.get("phone"+phone)==null){
            String code = UUID.randomUUID().toString().substring(0, 9);
            redisService.set("phone"+phone,code,60);
            smsService.sendMessage(phone,code);
        }else {
            throw new MyProjectException(MyProjectExceptionEnum.SMS_REPEAT);
        }
        return "发送成功";
    }
}
