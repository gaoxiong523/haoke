package com.gaoxiong.transaction;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName SpringTransactionConsumer
 * @Description TODO
 * @date 2019/8/19 11:30
 */
@Component
@RocketMQMessageListener(
        topic = "topic-spring-transaction",
        consumerGroup = "spring-transaction-group",
        selectorExpression = "*"
)
public class SpringTransactionConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage ( String message ) {
        System.out.println("message = " + message);
    }
}
