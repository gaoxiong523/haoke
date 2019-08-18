package com.gaoxiong.producer;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gaoxiong
 * @ClassName ProducerSpring
 * @Description TODO
 * @date 2019/8/18 0018 下午 9:34
 */
@Component
public class ProducerSpring {
    @Autowired
    private RocketMQTemplate  rocketMQTemplate;

    public void sendMsg ( String topic, String msg ) {
        rocketMQTemplate.convertAndSend(topic,msg );
    }
}
