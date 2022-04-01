package auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

/**
 * 注：若spring session 整合 spring security 的话会存在问题，使用 json 序列化则不能
 * 序列化 spring security 中的 DefaultSavedRequest ，使用 jdk 序列化则可以，
 * 但在认证微服务之外则会报错找不到这个类，同样不能实现 session 共享。
 * 目前采用在 gateway 网关处写一个 jwt 全局过滤器，无论有没有都放行，
 * 若没有则判断 token 是否正确，则 security 此处就不需要 jwt 过滤器了
 *
 * 注：使用 gateway 的全局过滤器网页相应就变得很慢，原因未知，采用
 * 管理员微服务用 security + jwt 认证结构
 * 消费端则采用第三方登录以及 spring session 共享实现单点登录
 */
@EnableRedisHttpSession
@Configuration
public class RedisSessionConfig {
    /**
     * 序列化机制选择json格式，默认使用jdk序列化。所有要保存的对象都要实现Serializable接口
     * 方法名不能修改
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * 自定义服务器返回给浏览器的cookie设置
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // 设置coolie名字
        serializer.setCookieName("redisSession");
        // 为了实现一处登录处处可用。将其有效域设置为顶级域 mall.com
        serializer.setDomainName("mall.com");
        return serializer;
    }

}
