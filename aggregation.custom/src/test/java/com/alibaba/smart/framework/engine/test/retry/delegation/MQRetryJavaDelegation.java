package com.alibaba.smart.framework.engine.test.retry.delegation;

import java.util.List;
import java.util.Map;

import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on 2017 November  11:46.
 */
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public abstract class MQRetryJavaDelegation implements JavaDelegation , MessageListenerConcurrently ,InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(MQRetryJavaDelegation.class);

    @Autowired
    private SmartEngine smartEngine;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        for (MessageExt messageExt : msgs) {


            try {

                consumeMessage(messageExt);

            } catch (Throwable throwable) {
                LOGGER.error(throwable.getMessage(),throwable);
                //如果抛出异常，默认通过metaQ来重试
                //FIXME
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    protected void consumeMessage(MessageExt messageExt) {
        processBusinessLogic(messageExt);
        signalProcess(messageExt);
    }

    protected void signalProcess(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        String bodyString = new String(body);
        LOGGER.info("signalProcess"+bodyString);
        DiqiBean diqiBean = null;;

        Long orderId = diqiBean.getOrderId();
        ProcessInstance processInstance = LazadaTest.getMockDB().get(orderId);
        String processInstanceId = processInstance.getInstanceId();
        String currentActivityId = diqiBean.getCurrentActivityId();
        Map map = null;


        //SmartEngine smartEngine = (SmartEngine)ApplicationContextUtil.getBean("smartEngine");

        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();


        signalCurrentActivity(  processInstance,processInstanceId, currentActivityId, map, executionQueryService, executionCommandService);

        processInstance = LazadaTest.getMockDB().get(orderId);

        //如果仍然是ReceiveTask，但是万一还是需要外部触发的呢？那么就不实现这个继承接口（比如支付宝信息）
        signalIfNextActivityIsReceiveTask(  processInstance,processInstanceId, map, smartEngine, executionQueryService,
            executionCommandService);


    }

    protected void signalIfNextActivityIsReceiveTask(ProcessInstance processInstance,String processInstanceId,  Map map, SmartEngine smartEngine,
                                                   ExecutionQueryService executionQueryService,
                                                   ExecutionCommandService executionCommandService) {
        //FIXME,do query again.
        try{
            PersisterSession.create().setProcessInstance(processInstance);

            List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecutionList(processInstanceId);
            boolean found = false;
            if(!CollectionUtils.isEmpty(executionInstanceList)){
                for (ExecutionInstance executionInstance : executionInstanceList) {
                    String processDefinitionActivityId =  executionInstance.getProcessDefinitionActivityId();

                    ProcessDefinition processDefinition = smartEngine.getRepositoryQueryService().getCachedProcessDefinition(executionInstance.getProcessDefinitionIdAndVersion());

                    List<BaseElement> baseElements =  processDefinition.getProcess().getElements();
                    for (BaseElement baseElement : baseElements) {
                        if(baseElement instanceof ReceiveTask){
                            ReceiveTask receiveTask = (ReceiveTask)baseElement;
                            Map<String, String> properties = receiveTask.getProperties();
                            String needListenerNormalMQMessage = null;
                            if(null != properties){
                                needListenerNormalMQMessage =  properties.get("needListenerNormalMQMessage");

                            }
                            if(processDefinitionActivityId.equals(receiveTask.getId()) && "no".equals(needListenerNormalMQMessage)){
                                found = true;

                                ProcessInstance newProcessInstance = executionCommandService.signal(executionInstance.getInstanceId(),map);
                                LazadaTest.getMockDB().put(LazadaTest.ORDER_ID,newProcessInstance);

                            }

                        }
                    }

                }
                if(!found){
                    LOGGER.error("No active executionInstance found for businessInstanceId "+123 );
                }

            }else{
                LOGGER.error("No active executionInstance found for businessInstanceId "+123 );
            }

        }finally {
            PersisterSession.currentSession().destroy();

        }

    }

    protected void signalCurrentActivity(ProcessInstance processInstance,String processInstanceId, String currentActivityId, Map map,
                                       ExecutionQueryService executionQueryService,
                                       ExecutionCommandService executionCommandService) {
        //FIXME,do query again.
        try{
            PersisterSession.create().setProcessInstance(processInstance);

            List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecutionList(processInstanceId);
            boolean found = false;
            if(!CollectionUtils.isEmpty(executionInstanceList)){
                for (ExecutionInstance executionInstance : executionInstanceList) {
                    if( executionInstance.getProcessDefinitionActivityId().equals(currentActivityId)){
                        found = true;

                        ProcessInstance newProcessInstance = executionCommandService.signal(executionInstance.getInstanceId(),map);

                        LazadaTest.getMockDB().put(LazadaTest.ORDER_ID,newProcessInstance);

                    }
                }
                if(!found){
                    LOGGER.error("No active executionInstance found for businessInstanceId "+123 +",currentActivityId "+currentActivityId);
                }

            }else{
                LOGGER.error("No active executionInstance found for businessInstanceId "+123 +",currentActivityId "+currentActivityId);
            }
        }finally {
            PersisterSession.currentSession().destroy();
        }


    }

    protected void processBusinessLogic(MessageExt messageExt) {


    }

    @Override
    public Object execute(ExecutionContext executionContext) {
        //CATCH EXCETPTION ,SEND MSG.
        return  null;
    }


}
