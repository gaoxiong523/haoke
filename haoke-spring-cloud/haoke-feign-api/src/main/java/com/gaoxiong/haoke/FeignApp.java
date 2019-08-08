package com.gaoxiong.haoke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author gaoxiong
 * @ClassName FeignApp
 * @Description TODO
 * @date 2019/8/7 0007 下午 10:33
 */
@SpringBootApplication
@EnableFeignClients
//@EnableDiscoveryClient
public class FeignApp {
    public static void main ( String[] args ) {
        SpringApplication.run(FeignApp.class, args);
    }
}
