package com.gaoxiong.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName TransactionListenerImpl
 * @Description TODO
 * @date 2019/8/19 11:07
 */
@RocketMQTransactionListener(txProducerGroup = "gaoxiong-transaction-group")
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    private static Map<String, RocketMQLocalTransactionState> STATE_MAP = new HashMap<>();

    /**
     * 执行本地业务逻辑
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction ( Message msg, Object arg ) {
        String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        try {
            System.out.println("执行操作1");
            Thread.sleep(30);

            System.out.println("执行操作2");
            Thread.sleep(800);

            STATE_MAP.put(transactionId, RocketMQLocalTransactionState.COMMIT);
            //模拟回查
            //Thread.sleep(1000000);
            return RocketMQLocalTransactionState.COMMIT;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        STATE_MAP.put(transactionId, RocketMQLocalTransactionState.ROLLBACK);
        return RocketMQLocalTransactionState.ROLLBACK;

    }

    /**
     * 检查本地事务的状态,消息系统如果没有收到事务的commit,或者 rollback
     * 将会来检查这个结果,根据这个结果决定消息是否发送给消费者
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction ( Message msg ) {
        String transactionId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        System.out.println("回查事务消息的transactionId = " + transactionId);
        System.out.println("事务的状态  STATE_MAP = " + STATE_MAP.get(transactionId));
        return STATE_MAP.get(transactionId);
    }
}
