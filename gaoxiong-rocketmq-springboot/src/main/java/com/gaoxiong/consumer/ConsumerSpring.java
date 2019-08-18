package com.gaoxiong.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName ConsumerSpring
 * @Description TODO
 * @date 2019/8/18 0018 下午 9:36
 */
@Component
@RocketMQMessageListener(consumerGroup = "gaoxiong-group-consumer",
topic = "topic-spring",selectorExpression = "*")
public class ConsumerSpring implements RocketMQListener<String> {


    @Override
    public void onMessage ( String s ) {
        System.out.println("s = " + s);
    }
}
