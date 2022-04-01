package customer.config;

import common.redisUtils.RedisService;
import common.redisUtils.impl.RedisServiceImpl;
import common.util.IDWorker;
import common.util.JwtTokenUtil;
import customer.interceptor.CartInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public CartInterceptor cartInterceptor(){
        return new CartInterceptor();
    }


    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil();
    }

    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }

    @Bean
    public IDWorker idWorker(){
        return new IDWorker(1,10);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartInterceptor());
    }
}
