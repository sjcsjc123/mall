package customer.controller;

import com.baomidou.mybatisplus.extension.api.R;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import third.service.OssService;

import java.util.Map;

/**
 * 测试oss
 */
@RestController
@RequestMapping("/oss")
public class OssController {

    @DubboReference
    public OssService ossService;

    @RequestMapping("/policy")
    public Map<String, String> policy() {
        Map<String, String> uploadPolicy = ossService.getUploadPolicy();
        return uploadPolicy;
    }
}
