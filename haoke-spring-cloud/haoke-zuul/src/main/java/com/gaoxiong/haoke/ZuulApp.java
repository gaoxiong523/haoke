package com.gaoxiong.haoke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author gaoxiong
 * @ClassName ZuulApp
 * @Description TODO
 * @date 2019/8/7 0007 下午 10:30
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulApp {
    public static void main ( String[] args ) {
        SpringApplication.run(ZuulApp.class, args);
    }
}
