package com.gaoxiong.websocket.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author gaoxiong
 * @ClassName MyHandler
 * @Description TODO
 * @date 2019/8/14 16:36
 */
@Component
public class MyHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished ( WebSocketSession session ) throws Exception {
       session.sendMessage(new TextMessage("来了老弟!!!!!!"));
    }

    @Override
    protected void handleTextMessage ( WebSocketSession session, TextMessage message ) throws Exception {
        String payload = message.getPayload();
        System.out.println("收到的消息是::" + payload);
        session.sendMessage(new TextMessage("消息已收到"));

        if (true) {
            System.out.println("进来了");
            for (int i = 0; i < 100; i++) {
                System.out.println("开始发送");
                session.sendMessage(new TextMessage("消息->" + i));
            }

        }
    }

    @Override
    public void afterConnectionClosed ( WebSocketSession session, CloseStatus status ) throws Exception {
        System.out.println("session:"+session.getId()+"----断开链接");
    }
}
