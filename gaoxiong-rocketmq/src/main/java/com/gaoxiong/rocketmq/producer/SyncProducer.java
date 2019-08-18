package com.gaoxiong.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.Charset;

/**
 * @author gaoxiong
 * @ClassName SyncProducer
 * @Description TODO
 * @date 2019/8/16 10:56
 */
public class SyncProducer {
    public static void main ( String[] args ) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("haoke-im");
        producer.setNamesrvAddr("192.168.150.131:9876;192.168.150.131:9877");
        producer.start();
        Message message = new Message("haoke-im-topic", "测试消息".getBytes(Charset.defaultCharset()));
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult.getMsgId());
        System.out.println(sendResult.getMessageQueue());
        System.out.println(sendResult.getOffsetMsgId());
        System.out.println(sendResult.getQueueOffset());
        System.out.println(sendResult.getSendStatus());
        System.out.println(sendResult.getRegionId());
        System.out.println(sendResult.getTransactionId());
        producer.shutdown();

    }
}
