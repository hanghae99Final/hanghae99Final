package org.sparta.mytaek1.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 origin을 설정합니다. 필요에 따라 "*"을 사용하여 모든 origin 허용 가능
        config.addAllowedOrigin("*");

        // 허용할 요청 메서드 설정
        config.addAllowedMethod("*");

        // 필요에 따라 다양한 설정 추가 가능

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
