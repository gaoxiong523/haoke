package com.gaoxiong.haoke;

import com.gaoxiong.haoke.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author gaoxiong
 * @ClassName HouseServiceApp
 * @Description TODO
 * @date 2019/8/7 0007 下午 10:14
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HouseServiceApp {
    public static void main ( String[] args ) {
        SpringApplication.run(HouseServiceApp.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
