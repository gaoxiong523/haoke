package com.gaoxiong.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.server.ServerEndpoint;

/**
 * @author gaoxiong
 * @ClassName WebSocketApp
 * @Description TODO
 * @date 2019/8/14 16:06
 */
@SpringBootApplication
public class WebSocketApp {
    public static void main ( String[] args ) {
        SpringApplication.run(WebSocketApp.class, args);
    }
}
