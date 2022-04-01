package gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 全局跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        // corsConfiguration 处理策略
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许所有请求头
        corsConfiguration.addAllowedHeader("*");
        // 允许所有来源
        corsConfiguration.addAllowedOrigin("*");
        // 允许所有请求方法
        corsConfiguration.addAllowedMethod("*");
        // 允许携带coolie
        corsConfiguration.setAllowCredentials(true);
        // path 要对哪些请求进行跨域处理；corsConfiguration 处理策略
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        // 创建过滤器
        return new CorsWebFilter(configurationSource);
    }
}
