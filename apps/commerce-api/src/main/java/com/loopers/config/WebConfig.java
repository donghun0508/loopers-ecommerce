package com.loopers.config;

import com.loopers.interfaces.api.UserApiHeaderValidationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserApiHeaderValidationInterceptor userApiHeaderValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userApiHeaderValidationInterceptor).addPathPatterns("/api/v1/users/{id}/**");
    }
}
