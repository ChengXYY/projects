package com.my.formtool.config;

import com.my.formtool.filter.AccessIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    AccessIntercepter accessIntercepter;

    final String[] notInterceptPaths = {"/login/**","/error/**" ,"/form/**","/css/**", "/js/**", "/images/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessIntercepter).addPathPatterns("/**").excludePathPatterns(notInterceptPaths);
    }

}
