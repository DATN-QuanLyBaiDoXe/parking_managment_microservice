package com.tth.id.config;

import com.tth.id.interceptor.RequestResponseHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RequestResponseHandlerInterceptor requestResponseHandlerInterceptor() {
        return new RequestResponseHandlerInterceptor();
    }

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory(){
        return new SimpleClientHttpRequestFactory();
    }

    @Bean
    public BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory(){
        return new BufferingClientHttpRequestFactory(simpleClientHttpRequestFactory());
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory());
        restTemplate.setInterceptors(Collections.singletonList(requestResponseHandlerInterceptor()));
        return restTemplate;
    }
}