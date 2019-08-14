package com.gaoxiong.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author gaoxiong
 * @ClassName WebSocketConfig
 * @Description TODO
 * @date 2019/8/14 16:16
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Autowired
    private MyHandler myHandler;

    @Override
    public void registerWebSocketHandlers ( WebSocketHandlerRegistry registry ) {
        registry.addHandler(myHandler, "/ws").setAllowedOrigins("*");
    }


}
