package com.gaoxiong.haoke.config;

import feign.Feign;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.netflix.ribbon.okhttp.OkHttpLoadBalancingClient;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author gaoxiong
 * @ClassName FeignConfig
 * @Description TODO
 * @date 2019/8/8 10:01
 */
@Configuration
@ConditionalOnBean(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(60, TimeUnit.SECONDS)
                //写超时
                .writeTimeout(60, TimeUnit.SECONDS)
                //读超时
                .readTimeout(60, TimeUnit.SECONDS)
                //重试
                .retryOnConnectionFailure(true)
                //连接池
                .connectionPool(new ConnectionPool())
                .build();
    }
}
