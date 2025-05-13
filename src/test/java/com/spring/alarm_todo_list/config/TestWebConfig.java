package com.spring.alarm_todo_list.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@TestConfiguration
public class TestWebConfig implements WebMvcConfigurer {

    private final TestLoginUserResolver testLoginUserResolver;

    public TestWebConfig(TestLoginUserResolver testLoginUserResolver) {
        this.testLoginUserResolver = testLoginUserResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(testLoginUserResolver);
    }
}