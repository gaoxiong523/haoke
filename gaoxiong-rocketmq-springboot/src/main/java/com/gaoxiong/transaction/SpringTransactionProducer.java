package com.gaoxiong.transaction;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName SpringTransactionProducer
 * @Description TODO
 * @date 2019/8/19 11:22
 */
@Component
public class SpringTransactionProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendTransactionMessage(String txProducerGroup,String topic,String msg){
        Message<String> stringMessage = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.sendMessageInTransaction(txProducerGroup, topic,stringMessage ,null );
        System.out.println(" 事务消息发送成功!= ");
    }
}
