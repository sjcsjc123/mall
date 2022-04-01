package third.service;

import java.util.Map;

/**
 * 整合oss实现文件上传
 */
public interface OssService {

    /**
     * 返回服务端的签名信息
     * @return
     */
    Map<String, String> getUploadPolicy();
}
