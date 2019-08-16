package com.gaoxiong.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.Charset;

/**
 * @author gaoxiong
 * @ClassName AsyncProducer
 * @Description TODO
 * @date 2019/8/16 11:16
 */
public class AsyncProducer {
    public static void main ( String[] args ) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("haoke-im");
        producer.setNamesrvAddr("106.12.84.126:9876");

        producer.start();

        Message message = new Message("haoke-im-topic", "send_msg", "用户a发信息给用户B".getBytes(Charset.defaultCharset()));

        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess ( SendResult sendResult ) {
                System.out.println("sendResult = " + sendResult);
            }

            @Override
            public void onException ( Throwable e ) {
                System.out.println("e = " + e.getMessage());
            }
        });

//        producer.shutdown();
    }
}
