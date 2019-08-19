package com.gaoxiong.producer;

import com.gaoxiong.transaction.SpringTransactionProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerSpringTest {

    @Autowired
    private ProducerSpring producerSpring;

    @Autowired
    private SpringTransactionProducer transactionProducer;

    @Test
    public void sendMsg () {
        String topic = "topic-spring";
        String msg = "baidu-spring的测试消息";
        producerSpring.sendMsg(topic,msg );
    }

    @Test
    public void send(){
        String txProducerGroup = "gaoxiong-transaction-group";
        String topic = "topic-spring-transaction";
        String msg = "spring的事务测试消息";
        transactionProducer.sendTransactionMessage(txProducerGroup,topic ,msg );

    }


}