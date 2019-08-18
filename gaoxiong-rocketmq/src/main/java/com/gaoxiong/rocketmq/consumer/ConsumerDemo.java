package com.gaoxiong.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.Set;

/**
 * @author gaoxiong
 * @ClassName ConsumerDemo
 * @Description TODO
 * @date 2019/8/16 11:50
 */
public class ConsumerDemo {
    public static void main ( String[] args ) throws MQClientException {
        DefaultMQPushConsumer pullConsumer = new DefaultMQPushConsumer("haoke-im");
        pullConsumer.setNamesrvAddr("192.168.150.131:9876;192.168.150.131:9877");
        pullConsumer.subscribe("haoke-im-topic", "*");
//        Set<MessageQueue> messageQueues = pullConsumer.fetchSubscribeMessageQueues("haoke-im-topic");

        pullConsumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage ( List<MessageExt> msgs, ConsumeOrderlyContext context ) {
                for (MessageExt msg : msgs) {
                    System.out.println("msg = " + msg);
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        pullConsumer.start();
    }
}
