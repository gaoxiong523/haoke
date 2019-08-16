package com.gaoxiong.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author gaoxiong
 * @ClassName OrderProducer
 * @Description TODO
 * @date 2019/8/16 13:27
 */
public class OrderProducer {
    public static void main ( String[] args ) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("haoke-order-producer");
        producer.setNamesrvAddr("106.12.84.126:9876");
        producer.setSendMessageWithVIPChannel(true);

        producer.start();

        for (int i = 100; i < 200; i++) {
            String msgStr = "order id => " + i;
            int orderId = i % 10;//模拟生成订单,取余
            Message message = new Message("haoke-order-topic", msgStr.getBytes(Charset.defaultCharset()));
            SendResult sendResult = producer.send(message, ( mqs, msg, arg ) -> {
                Integer id = (Integer) arg;
                int index = id % mqs.size();
                return mqs.get(index);
            }, orderId);
            System.out.println("sendResult = " + sendResult);
        }
        producer.shutdown();
    }
}
