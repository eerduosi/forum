package com.forum.config;

import com.forum.interceptor.LoginTicketInterceptor;
import com.forum.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(myInterceptor)

                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg")

                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)

                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");


    }

}
