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
        String appcode = "fdd9d8d416f4455ab0dd73289bf4c450";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+"**minute**:1");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
