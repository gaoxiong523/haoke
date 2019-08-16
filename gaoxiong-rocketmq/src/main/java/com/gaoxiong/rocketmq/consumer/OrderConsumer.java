package com.gaoxiong.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author gaoxiong
 * @ClassName OrderConsumer
 * @Description TODO
 * @date 2019/8/16 13:41
 */
public class OrderConsumer {
    public static void main ( String[] args )throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("haoke-order");
        consumer.setNamesrvAddr("106.12.84.126:9876");
        consumer.subscribe("haoke-order-topic", "*");

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage ( List<MessageExt> msgs, ConsumeOrderlyContext context ) {
                System.out.println(Thread.currentThread().getName()+"msgs = " + msgs);
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
    }
}
