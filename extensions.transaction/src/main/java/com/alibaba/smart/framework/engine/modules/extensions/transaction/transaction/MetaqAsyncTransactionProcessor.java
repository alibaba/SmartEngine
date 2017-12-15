package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;


import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.smart.framework.engine.modules.extensions.transaction.util.SerializeUtils;
import com.taobao.metaq.client.MetaProducer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class MetaqAsyncTransactionProcessor extends AbstractTransactionProcessor {


    private String topic;
    private String publishGroup;
    private MetaProducer metaProducer;

    public MetaqAsyncTransactionProcessor(String t, String g) {

        Assert.isTrue(!StringUtils.isEmpty(t), "topic can not be empty!");
        Assert.isTrue(!StringUtils.isEmpty(g), "publishGroup can not be empty!");

        this.topic = t;
        this.publishGroup = g;

        metaProducer = new MetaProducer(publishGroup);
        try {
            metaProducer.start();
        } catch (Exception e) {
            throw new RuntimeException("init meta client error!" + e.getMessage());
        }

    }

    @Override
    public void saveForRollback(TransactionProcessContext context) {
        sendMessage("rollback", context);
    }


    @Override
    public void saveForRedo(TransactionProcessContext context) {
        sendMessage("redo", context);
    }

    private void sendMessage(String tag, TransactionProcessContext context) {
        try {

            byte[] bytes = SerializeUtils.serialize(context);

            Message message = new Message(
                    topic,
                    tag,
                    "",
                    bytes
            );

            SendResult sendResult = metaProducer.send(message);
            if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                logger.info("send message for " + tag + " success,msgId=" + sendResult.getMsgId());
            } else {
                logger.error("send message for " + tag + " fail,context=" + context + ",result=" + sendResult);
            }

        } catch (Exception e) {
            logger.error("save for " + tag + " error", e);
        }

    }


}
