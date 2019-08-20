//package com.alibaba.smart.framework.engine.test.retry.delegation;
//
//import com.alibaba.rocketmq.common.message.MessageExt;
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//
//import com.taobao.metaq.client.MetaPushConsumer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
///**
// * Created by 高海军 帝奇 74394 on 2017 November  11:46.
// */
//
//@Service
//public  class PaymentMQRetryJavaDelegation extends  MQRetryJavaDelegation{
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentMQRetryJavaDelegation.class);
//
//    //private  MetaProducer producer;
//
//    public PaymentMQRetryJavaDelegation(){
//        System.out.printf("hello ");
//    }
//
//    private void buildConsumer(String consumerGroup,String topic,String tag) {
//
//        try {
//            //TODO 多个连接是否有问题
//            //
//            //producer = new MetaProducer(produceGroup);
//            //
//            //producer.start();
//
//            MetaPushConsumer consumer = new MetaPushConsumer(consumerGroup);
//
//            consumer.subscribe(topic, tag);
//            consumer.registerMessageListener(this);
//            consumer.start();
//            LOGGER.info("Init MetaQ success.");
//        } catch (Exception e) {
//            LOGGER.error("Init MetaQ failed:" + e.getMessage(), e);
//        }
//    }
//
//
//
//    protected void processBusinessLogic(MessageExt messageExt) {
//        LazadaTest.getHolder().add("update db,reduce inventory..blalba");
//    }
//
//
//    @Override
//    public Object execute(ExecutionContext executionContext) {
//        //CATCH EXCETPTION ,SEND MSG.
//        return  null;
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//        buildConsumer(LazadaTest.PAYMENT_GROUP4,LazadaTest.PAYMENT_TOPIC4,null);
//    }
//}
