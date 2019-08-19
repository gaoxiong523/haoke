package com.gaoxiong.haoke.im.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName MyHandshakeInterceptor
 * @Description 自定义握手拦截器
 * @date 2019/8/15 13:38
 */
@Component
public class MyHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake ( ServerHttpRequest request,
                                     ServerHttpResponse response,
                                     WebSocketHandler wsHandler,
                                     Map<String, Object> attributes ) throws Exception {
        System.out.println(" 开始握手" );
        return true;
    }

    @Override
    public void afterHandshake ( ServerHttpRequest request,
                                 ServerHttpResponse response,
                                 WebSocketHandler wsHandler, Exception exception ) {
        System.out.println("握手成功啦!");

    }
}
