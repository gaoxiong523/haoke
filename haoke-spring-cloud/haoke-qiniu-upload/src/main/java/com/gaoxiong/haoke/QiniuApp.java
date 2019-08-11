package com.gaoxiong.haoke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author gaoxiong
 * @ClassName QiniuApp
 * @Description TODO
 * @date 2019/8/10 0010 下午 4:34
 */
@SpringBootApplication
@EnableDiscoveryClient
public class QiniuApp {
    public static void main ( String[] args ) {
        SpringApplication.run(QiniuApp.class, args);
    }
}
