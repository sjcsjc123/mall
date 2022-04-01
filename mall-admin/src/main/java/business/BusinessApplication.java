package business;

import common.redisUtils.RedisService;
import common.redisUtils.impl.RedisServiceImpl;
import common.util.IDWorker;
import common.util.JwtTokenUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("business.mapper")
public class BusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class,args);
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
        return new IDWorker(1,3);
    }
}
