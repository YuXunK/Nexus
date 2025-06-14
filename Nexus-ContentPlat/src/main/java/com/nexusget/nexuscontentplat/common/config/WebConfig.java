package com.nexusget.nexuscontentplat.common.config;

import com.nexusget.nexuscontentplat.web.interceptor.SensitiveWordInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebFluxConfigurer {
    private final SensitiveWordInterceptor interceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/comments", "/articles", "/posts/*");
    }
}
