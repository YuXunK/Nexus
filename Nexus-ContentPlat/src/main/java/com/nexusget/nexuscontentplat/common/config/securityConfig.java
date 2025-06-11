package com.nexusget.nexuscontentplat.common.config;

import com.nexusget.nexuscontentplat.common.filter.TokenValidationGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class securityConfig {

    private final TokenValidationGlobalFilter tokenValidationGlobalFilter;
    public securityConfig(TokenValidationGlobalFilter tokenValidationGlobalFilter) {
        this.tokenValidationGlobalFilter = tokenValidationGlobalFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF（API服务通常不需要）
                .csrf(AbstractHttpConfigurer::disable)

                // 授权规则配置
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()    // 登录接口公开
                        .requestMatchers("/swagger-ui/**").permitAll()  // Swagger文档
                        .requestMatchers("/v3/api-docs/**").permitAll() // OpenAPI文档
                        .requestMatchers("/error").permitAll()          // 错误端点
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // 需要ADMIN角色
                        .anyRequest().authenticated()                   // 其他请求需认证
                )
                // 添加自定义Token过滤器
                .addFilterBefore(tokenValidationGlobalFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}