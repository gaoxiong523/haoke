package com.gaoxiong.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gaoxiong
 * @ClassName OrderConsumer
 * @Description TODO
 * @date 2019/8/16 13:41
 */
public class OrderConsumerFetch {

    private static final Map<MessageQueue,Long> OFFSE_TABLE = new HashMap<MessageQueue,Long>();

    public static void main ( String[] args ) throws Exception {
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("haoke-order");
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("haoke-order");
        consumer.setNamesrvAddr("106.12.84.126:9876");
        consumer.start();
        // 消息队列
        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("haoke-order-topic");

        for (MessageQueue messageQueue : messageQueues) {

            new Thread(() -> {
                try {
                    long offset = consumer.fetchConsumeOffset(messageQueue, true);
                    System.out.println("consumer from " + messageQueue.getQueueId() + "    offset = " + offset);
                    while (true) {
//                        PullResult pullResult = consumer.pull(messageQueue, "*", getMessageQueueOffset(messageQueue), 32);
                        PullResult pullResult = consumer.pullBlockIfNotFound(messageQueue, "*", getMessageQueueOffset(messageQueue), 32);
                        putMessageQueueOffset(messageQueue,pullResult.getNextBeginOffset());
                        switch (pullResult.getPullStatus()) {
                            case FOUND:
                                List<MessageExt> msgFoundList = pullResult.getMsgFoundList();
                                for (MessageExt messageExt : msgFoundList) {
                                    System.out.println(Thread.currentThread().getName()+"===="+"消息消费次数===>"+messageExt.getReconsumeTimes());
                                    System.out.println(Thread.currentThread().getName()+"===="+"拉取到的消息是::messageExt = " + new String(messageExt.getBody(), Charset.defaultCharset()));
                                }
                                break;
                            case NO_NEW_MSG:
                                System.out.println(Thread.currentThread().getName()+"===="+"没有新消息了");
                                break;
                            case NO_MATCHED_MSG:
                                System.out.println(Thread.currentThread().getName()+"===="+Thread.currentThread().getName()+"===="+"没有匹配的消息");
                                break;
                            case OFFSET_ILLEGAL:
                                System.out.println(Thread.currentThread().getName()+"===="+"offset偏移量非法");
                            default:
                        }
                    }
                } catch (MQClientException e) {
                    e.printStackTrace();
                } catch (RemotingException e) {
                    e.printStackTrace();
                } catch (MQBrokerException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        }


    }

    /**
     * 保存上次消费的消息下标
     * @param messageQueue
     * @param nextBeginOffset
     */
    private static void putMessageQueueOffset ( MessageQueue messageQueue, long nextBeginOffset ) {
        OFFSE_TABLE.put(messageQueue, nextBeginOffset);
    }

    private static Long getMessageQueueOffset(MessageQueue messageQueue){
        Long offset = OFFSE_TABLE.get(messageQueue);
        if (offset != null) {
            return offset;
        }
        return 01L;
    }
}
