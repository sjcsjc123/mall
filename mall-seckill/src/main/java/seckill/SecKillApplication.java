package seckill;

import common.redisUtils.RedisService;
import common.redisUtils.impl.RedisServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("seckill.mapper")
public class SecKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class,args);
    }

    @Bean
    public RedisService redisService(){
        return new RedisServiceImpl();
    }

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        config.useSingleServer()
                //可以用"rediss://"来启用SSL连接
                .setAddress("redis://127.0.0.1:6379");
        /*config.useClusterServers()
                .setScanInterval(2000)
                .addNodeAddress("redis://192.168.41.131:6379",
                        "redis://192.168.41.131:6380",
                        "redis://192.168.41.131:6381",
                        "redis://192.168.41.131:6382",
                        "redis://192.168.41.131:6383",
                        "redis://192.168.41.131:6384");*/
        return Redisson.create(config);
    }
}
