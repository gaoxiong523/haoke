package com.gaoxiong.haoke.im.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaoxiong.haoke.im.pojo.Message;
import com.gaoxiong.haoke.im.pojo.UserData;
import com.gaoxiong.haoke.im.service.MessageService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName MessageHandler
 * @Description 自定义消息处理
 * @date 2019/8/15 17:26
 */
@Component
@RocketMQMessageListener(
        topic = "haoke-im-send-message-topic",
        consumerGroup = "haoke-im-consumer-group",
        selectorExpression = "SEND_MSG"
)
public class MessageHandler extends TextWebSocketHandler implements RocketMQListener<Message> {

    @Autowired
    private MessageService messageService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Map<Long, WebSocketSession> SESSION_MAP = new HashMap<>();

    @Override
    public void afterConnectionEstablished ( WebSocketSession session ) throws Exception {
        Long uid = (Long) session.getAttributes().get("uid");
        //将当前用户的session放入map中
        SESSION_MAP.put(uid, session);
    }

    @Override
    protected void handleTextMessage ( WebSocketSession session, TextMessage message ) throws Exception {
        Long uid = (Long) session.getAttributes().get("uid");
        String payload = message.getPayload();
        JsonNode jsonNode = MAPPER.readTree(payload);
        Long toId = jsonNode.get("toId").asLong();
        String msg = jsonNode.get("msg").asText();
        //构造消息体
        Message build = Message.builder()
                .msg(msg)
                .from(UserData.USER_MAP.get(uid))
                .to(UserData.USER_MAP.get(toId))
                .build();
        //保存到Mongo中
        Message saveMessage = messageService.saveMessage(build);

        //判断用户是否在线
        WebSocketSession toSession = SESSION_MAP.get(toId);
        if (toSession != null && toSession.isOpen()) {
            //发送给用户
            toSession.sendMessage(new TextMessage(MAPPER.writeValueAsString(saveMessage)));
            //更新消息状态,todo 消息的状态是否包括已发送,未发送,已读,未读
            messageService.updateMessage(saveMessage.getId().toHexString(), 1);
        } else {
            //构建消息,发送
            org.springframework.messaging.Message<Message> build1 = MessageBuilder.withPayload(saveMessage).build();
            //topic:tags 设置主题和标签
            rocketMQTemplate.send("haoke-im-send-message-topic:SEND_MSG", build1);
        }
    }


    @Override
    public void onMessage ( Message message ) {
        System.out.println("从消息系统收到的消息是  " +"message = " + message);
        try {
            Long toId = message.getTo().getId();
            WebSocketSession toSession = SESSION_MAP.get(toId);
            //判断用户是否在线
            if (toSession != null&& toSession.isOpen()) {
                //发送消息
                toSession.sendMessage(new TextMessage(message.toString()));
                //更新消息状态
                this.messageService.updateMessage(message.getId().toString(), 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
