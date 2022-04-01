package third.service;

/**
 * 整合短信服务
 */
public interface SmsService {

    /**
     * 发送短信
     */
    void sendMessage(String phone,String code);
}
