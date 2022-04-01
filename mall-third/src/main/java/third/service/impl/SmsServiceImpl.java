package third.service.impl;

import org.springframework.stereotype.Component;
import third.service.SmsService;
import third.utils.HttpUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

@DubboService
@Component
public class SmsServiceImpl implements SmsService {

    /**
     * 其他地方调用第三方服务即可，实际开发中可修改模板，将签名改为企业
     * 见customer模块
     * @param phone
     * @param code
     */
    @Override
    public void sendMessage(String phone, String code) {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+"**minute**:1");
        querys.put("smsSignId", "");
        querys.put("templateId", "");
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
