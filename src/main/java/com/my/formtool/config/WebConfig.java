package com.my.formtool.config;

import com.my.formtool.filter.AccessIntercepter;
import com.my.formtool.filter.LoginIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    LoginIntercepter loginIntercepter;

    @Autowired
    AccessIntercepter accessIntercepter;

    final String[] notInterceptPaths = {"/login/**","/error/**" ,"/css/**", "/js/**", "/images/**"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginIntercepter).addPathPatterns("/**").excludePathPatterns(notInterceptPaths);
        registry.addInterceptor(accessIntercepter).addPathPatterns("/**").excludePathPatterns(notInterceptPaths);
    }

}
