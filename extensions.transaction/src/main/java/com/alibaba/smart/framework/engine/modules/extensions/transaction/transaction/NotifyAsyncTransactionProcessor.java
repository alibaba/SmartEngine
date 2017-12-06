package com.alibaba.smart.framework.engine.modules.extensions.transaction.transaction;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;

import javax.annotation.Resource;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class NotifyAsyncTransactionProcessor extends AbstractTransactionProcessor {


//    @Resource
//    private TransactionTaskRetryMetaqSender transactionTaskRetryMetaqSender;

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

//            byte[] bytes = SerializeUtils.serialize(context);
//
//            Message message = new Message(
//                    TransactionTaskRetryMetaqSender.META_PUB_TOPIC,
//                    tag,
//                    "",
//                    bytes
//            );
//
//            SendResult sendResult = transactionTaskRetryMetaqSender.send(message);
//            if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
//                logger.info("send message for " + tag + " success,msgId=" + sendResult.getMsgId());
//            } else {
//                logger.error("send message for " + tag + " fail" + context);
//            }

        } catch (Exception e) {
            logger.error("save for " + tag + " error", e);
        }

    }

}
