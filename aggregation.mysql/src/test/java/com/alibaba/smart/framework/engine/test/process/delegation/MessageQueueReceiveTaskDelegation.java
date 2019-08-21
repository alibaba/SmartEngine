//package com.alibaba.smart.framework.engine.test.process.delegation;
//
//import java.util.List;
//
//import com.alibaba.rocketmq.client.consumer.hook.ConsumeConcurrentlyContext;
//import com.alibaba.rocketmq.client.consumer.hook.ConsumeConcurrentlyStatus;
//import com.alibaba.rocketmq.client.consumer.hook.MessageListenerConcurrently;
//import com.alibaba.rocketmq.common.message.MessageExt;
//import com.alibaba.smart.framework.engine.SmartEngine;
//import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
//import com.alibaba.smart.framework.engine.test.process.FullMultiInstanceTest;
//
//import com.taobao.metaq.client.MetaPushConsumer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * @author 帝奇
// */
//public class MessageQueueReceiveTaskDelegation implements JavaDelegation,MessageListenerConcurrently {
//
//    @Autowired
//    private SmartEngine smartEngine;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueueReceiveTaskDelegation.class);
//
//    private void buildConsumer(String consumerGroup,String topic,String tag) {
//
//            try {
//                MetaPushConsumer consumer = new MetaPushConsumer(consumerGroup);
//
//                consumer.subscribe(topic, tag);
//                consumer.registerMessageListener(this);
//                consumer.start();
//                LOGGER.info("buildMetaConsumer success.");
//            } catch (Exception e) {
//                LOGGER.error("buildMetaConsumer error:" + e.getMessage(), e);
//            }
//    }
//
//
//    @Override
//    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//
//        for (MessageExt messageExt : msgs) {
//
//            try {
//                messageExt.getBody();
//
//                //do business
//
//                //smartEngine.getExecutionCommandService().signal();
//
//            } catch (Throwable throwable) {
//                LOGGER.error(throwable.getMessage(),throwable);
//                //如果抛出异常，默认通过metaQ来重试
//                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//            }
//        }
//        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//    }
//
//    @Override
//    public Object execute(ExecutionContext executionContext) {
//        String text= (String)executionContext.getRequest().get("text");
//        FullMultiInstanceTest.trace.add(text);
//        return text;
//    }
//
//
//
//}
