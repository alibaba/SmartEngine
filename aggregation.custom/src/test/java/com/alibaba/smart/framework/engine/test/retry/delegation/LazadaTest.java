//package com.alibaba.smart.framework.engine.test.retry.delegation;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.alibaba.rocketmq.client.consumer.hook.ConsumeConcurrentlyContext;
//import com.alibaba.rocketmq.client.consumer.hook.ConsumeConcurrentlyStatus;
//import com.alibaba.rocketmq.client.consumer.hook.MessageListenerConcurrently;
//import com.alibaba.rocketmq.common.message.Message;
//import com.alibaba.rocketmq.common.message.MessageExt;
//import com.alibaba.smart.framework.engine.SmartEngine;
//import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
//import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
//import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
//import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
//import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
//import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
//import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
//import com.alibaba.smart.framework.engine.test.orchestration.ServiceOrchestrationJavaDelegation;
//
//import com.taobao.metaq.client.MetaProducer;
//import lombok.Getter;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@ContextConfiguration("/spring/service-orchestration-test.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//@Service
//public class LazadaTest extends  MQRetryJavaDelegation {
//    private static final Logger LOGGER = LoggerFactory.getLogger(MQRetryJavaDelegation.class);
//    public static final String PAYMENT_GROUP4 = "a0743941aaaa";
//    public static final String PAYMENT_TOPIC4 = "a0743941aaaa";
//    //public static final String PAYMENT_TAG3 = "a074394";
//
//    public static final String LAZADA_DIQI_security_GROUP = "b074394";
//    public static final String LAZADA_DIQI_security_TOPIC = "b074394";
//    public static final String LAZADA_DIQI_1111_security_TAG = "b074394";
//
//
//    private  MetaProducer producer;
//
//    @Getter
//    private  static  List<String> holder = new ArrayList<String>();
//
//    @Getter
//    private  static  Map<Long,ProcessInstance> mockDB = new HashMap<Long, ProcessInstance>();
//
//    @Getter
//    public  final static  Long ORDER_ID = 12345678L;
//
//    @Before
//    public void before(){
//        ServiceOrchestrationJavaDelegation.getArrayList().clear();
//
//        try {
//            producer = new MetaProducer(PAYMENT_GROUP4);
//
//            producer.start();
//
//            //MetaPushConsumer consumer = new MetaPushConsumer(PAYMENT_GROUP4);
//            //
//            //consumer.subscribe(PAYMENT_TOPIC4, "NON_EXSISTED_TAG");
//            //consumer.registerMessageListener(this);
//            //consumer.start();
//            LOGGER.info("Init MetaQ success.");
//        } catch (Exception e) {
//           throw new RuntimeException(e);
//        }
//
//    }
//
//    @Autowired
//    private SmartEngine smartEngine;
//
//    @Test
//	public void testExclusive() throws Exception {
//
//
//
//		ProcessCommandService processService = smartEngine.getProcessCommandService();
//        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
//        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();
//
//		Map<String, Object> request = new HashMap<String, Object>();
//		request.put("input", "first");
//
//        ProcessInstance processInstance = startProcess(processService, request);
//
//        List<String> arrayList = 	ServiceOrchestrationJavaDelegation.getArrayList();
//        Assert.assertEquals(2,arrayList.size());
//        Assert.assertEquals("first",arrayList.get(0));
//        Assert.assertEquals("first",arrayList.get(1));
//
//        checkCurrentIsPayment(executionQueryService);
//
//        //DiqiBean diqiBean = new DiqiBean();
//        //diqiBean.setMsg("money_receivedabc");
//        //diqiBean.setCurrentActivityId("WaitPayCallBackActivity");
//        //diqiBean.setOrderId(ORDER_ID);
//        //String letter = JacksonUtils.serialize(diqiBean);
//        //
//        //Message msg = new Message(PAYMENT_TOPIC4, null,letter.getBytes());
//        //producer.send(msg);
//
//        //Thread.currentThread().sleep(5000L);
//
//
//
//        processInstance = LazadaTest.getMockDB().get(ORDER_ID);
//        String processInstanceId = processInstance.getInstanceId();
//        String currentActivityId = "WaitPayCallBackActivity";
//        Map map = null;
//
//        String serializedProcessInstance = InstanceSerializerFacade.serialize(processInstance);
//        processInstance = InstanceSerializerFacade.deserializeAll(serializedProcessInstance);
//
//        signalCurrentActivity(  processInstance,processInstanceId, currentActivityId, map, executionQueryService, executionCommandService);
//
//        try{
//            PersisterSession.create();
//            ProcessInstance processInstance1 = getMockDB().get(ORDER_ID);
//            PersisterSession.currentSession().setProcessInstance(processInstance1);
//            List<ExecutionInstance> executionInstanceList =   executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
//            Assert.assertEquals(1,executionInstanceList.size());
//            ExecutionInstance  executionInstance =   executionInstanceList.get(0);
//            Assert.assertEquals("SecurityCheckActivity",executionInstance.getProcessDefinitionActivityId());
//        }finally {
//            PersisterSession.destroySession();
//
//        }
//
//
//        processInstance = LazadaTest.getMockDB().get(ORDER_ID);
//
//        //如果仍然是ReceiveTask，但是万一还是需要外部触发的呢？那么就不实现这个继承接口（比如支付宝信息）
//        signalIfNextActivityIsReceiveTask(  processInstance,processInstanceId, map, smartEngine, executionQueryService,
//            executionCommandService);
//
//
//
//
//        try{
//            PersisterSession.create();
//            ProcessInstance processInstance1 = getMockDB().get(ORDER_ID);
//            PersisterSession.currentSession().setProcessInstance(processInstance1);
//            List<ExecutionInstance> executionInstanceList =   executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
//            Assert.assertEquals(1,executionInstanceList.size());
//            ExecutionInstance  executionInstance =   executionInstanceList.get(0);
//            Assert.assertEquals("WaitPeopleCheckActivity",executionInstance.getProcessDefinitionActivityId());
//        }finally {
//            PersisterSession.destroySession();
//
//        }
//
//    }
//
//    private void checkCurrentIsPayment(ExecutionQueryService executionQueryService) {
//        try{
//            PersisterSession.create();
//            ProcessInstance processInstance1 = mockDB.get(ORDER_ID);
//            PersisterSession.currentSession().setProcessInstance(processInstance1);
//            List<ExecutionInstance> executionInstanceList =   executionQueryService.findActiveExecutionList(processInstance1.getInstanceId());
//            Assert.assertEquals(1,executionInstanceList.size());
//            ExecutionInstance  executionInstance =   executionInstanceList.get(0);
//            Assert.assertEquals("WaitPayCallBackActivity",executionInstance.getProcessDefinitionActivityId());
//        }finally {
//            PersisterSession.destroySession();
//
//        }
//    }
//
//    private ProcessInstance startProcess(ProcessCommandService processService, Map<String, Object> request) {
//        ProcessInstance processInstance = null;
//        try{
//            PersisterSession.create();
//             processInstance = processService.start(
//                "lazada", "1.0.0",
//                request);
//            mockDB.put(ORDER_ID,processInstance);
//        }finally {
//            PersisterSession.destroySession();
//
//        }
//        return processInstance;
//    }
//
//    @After
//    public void tearDown(){
//        ServiceOrchestrationJavaDelegation.getArrayList().clear();
//    }
//
//    @Override
//    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//        throw  new RuntimeException("not touch here");
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//    }
//}