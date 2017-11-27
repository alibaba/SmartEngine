package com.alibaba.smart.framework.engine.modules.smart.provider.performer;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.ContextBoundedJavaDelegation;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

import com.taobao.metaq.client.MetaPushConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author 帝奇
 */
public abstract   class MessageQueueReceiveTaskDelegation extends ContextBoundedJavaDelegation implements MessageListenerConcurrently,InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueueReceiveTaskDelegation.class);

    private void buildConsumer(String group,String topic,String tag) {

            try {
                MetaPushConsumer consumer = new MetaPushConsumer(group);

                consumer.subscribe(topic, tag);
                consumer.registerMessageListener(this);
                consumer.start();
                LOGGER.info("buildMetaConsumer success.");
            } catch (Exception e) {
                LOGGER.error("buildMetaConsumer error:" + e.getMessage(), e);
            }
    }




    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        for (MessageExt messageExt : msgs) {

            try {
                messageExt.getBody();

                //do business


                //smartEngine.getExecutionCommandService().signal();

            } catch (Throwable throwable) {
                LOGGER.error(throwable.getMessage(),throwable);
                //如果抛出异常，默认通过metaQ来重试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public  void afterPropertiesSet() throws Exception{

    }


}
