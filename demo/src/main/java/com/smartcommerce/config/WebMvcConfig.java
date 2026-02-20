package com.smartcommerce.config;

import com.smartcommerce.security.RequiredRoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequiredRoleInterceptor requiredRoleInterceptor;

    @Autowired
    public WebMvcConfig(RequiredRoleInterceptor requiredRoleInterceptor) {
        this.requiredRoleInterceptor = requiredRoleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requiredRoleInterceptor)
                .addPathPatterns("/api/**");
    }
}

