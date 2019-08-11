package com.gaoxiong.haoke.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author gaoxiong
 * @ClassName WebConfig
 * @Description TODO
 * @date 2019/8/10 0010 下午 10:11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxInMemorySize(4096);
        resolver.setMaxUploadSize(104857600);
        return resolver;
    }
}
