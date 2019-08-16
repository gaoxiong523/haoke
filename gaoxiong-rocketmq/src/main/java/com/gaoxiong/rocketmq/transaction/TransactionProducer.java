package com.gaoxiong.rocketmq.transaction;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoxiong
 * @ClassName TransactionProducer
 * @Description 分布式事务消息
 * @date 2019/8/16 15:34
 */
public class TransactionProducer {

    public static void main ( String[] args ) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("transaction-producer");
        producer.setNamesrvAddr("106.12.84.126:9876");
        //设置事务监听器
        producer.setTransactionListener(new TransactionListenerImpl());
        producer.start();
        Message message = new Message("pay-topic", "gaoxiong给zhangsan转账100".getBytes(Charset.defaultCharset()));
        TransactionSendResult transactionSendResult = producer.sendMessageInTransaction(message, null);
        if (transactionSendResult.getSendStatus().equals(SendStatus.SEND_OK)) {
            producer.shutdown();
        }
    }

    static class TransactionListenerImpl implements TransactionListener{

        private static  Map<String, LocalTransactionState> STATE_MAP = new HashMap<>();

        /**
         * 执行具体的业务
         * @param msg
         * @param arg
         * @return
         */
        @Override
        public LocalTransactionState executeLocalTransaction ( Message msg, Object arg ) {

            try {
                System.out.println("模拟用户A账户减500");
                Thread.sleep( 500);

                //模拟出错
//                System.out.println(1/0);
                System.out.println("模拟用户B账户加500");
                Thread.sleep(800);
                STATE_MAP.put(msg.getTransactionId(), LocalTransactionState.COMMIT_MESSAGE);
                //二次提交确认
                return LocalTransactionState.COMMIT_MESSAGE;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            STATE_MAP.put(msg.getTransactionId(), LocalTransactionState.ROLLBACK_MESSAGE);

            //回滚
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        /**
         * 消息回查
         * @param msg
         * @return
         */
        @Override
        public LocalTransactionState checkLocalTransaction ( MessageExt msg ) {
            return STATE_MAP.get(msg.getTransactionId());
        }
    }
}
