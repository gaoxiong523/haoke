package com.gaoxiong.rocketmq.transaction;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName TransactionConsumer
 * @Description TODO
 * @date 2019/8/16 16:42
 */
public class TransactionConsumer {
    public static void main ( String[] args ) throws  Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("transaction-consumer");
        consumer.setNamesrvAddr("106.12.84.126:9876");
        consumer.subscribe("pay-topic", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage ( List<MessageExt> msgs, ConsumeConcurrentlyContext context ) {
                for (MessageExt msg : msgs) {
                    System.out.println(new String(msg.getBody(), Charset.defaultCharset()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
