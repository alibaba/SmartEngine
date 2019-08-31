//package com.alibaba.smart.framework.engine.test.retry.delegation;
//
//import java.util.HashMap;
//import java.util.Map;
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
//public  class SecurityMQRetryJavaDelegation extends  MQRetryJavaDelegation{
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityMQRetryJavaDelegation.class);
//
//    //private  MetaProducer producer;
//
//    public SecurityMQRetryJavaDelegation(){
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
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//    protected void processBusinessLogic(MessageExt messageExt) {
//
//        LazadaTest.getHolder().add("update db,reduce inventory..blalba");
//    }
//
//
//    @Override
//    public Object execute(ExecutionContext executionContext) {
//        //CATCH EXCETPTION ,SEND MSG.
//
//
//
//       if(null == executionContext.getRequest()){
//           Map<String, Object> request = new HashMap<String, Object>();
//           executionContext.setRequest(request);
//       }
//        executionContext.getRequest().put("securityStatus","WAIT_PEOPLE_CHECK");
//
//        return  null;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        buildConsumer(LazadaTest.LAZADA_DIQI_security_GROUP,LazadaTest.LAZADA_DIQI_security_TOPIC,LazadaTest
//        .LAZADA_DIQI_1111_security_TAG);
//    }
//}
