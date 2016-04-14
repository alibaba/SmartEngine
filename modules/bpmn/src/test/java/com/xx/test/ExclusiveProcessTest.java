package com.xx.test;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.smart.framework.process.model.bpmn.ProcessDefinition;
import com.alibaba.smart.framework.process.model.bpmn.event.EndEvent;
import com.alibaba.smart.framework.process.model.bpmn.event.StartEvent;
import com.alibaba.smart.framework.process.model.bpmn.sequenceflow.ProcessSequenceFlow;
import com.alibaba.smart.framework.process.model.bpmn.task.ServiceTask;


public class ExclusiveProcessTest {
    private ProcessDefinition processDefinition;
    
    @Before
    public void before(){
        processDefinition = new ProcessDefinition();
        processDefinition.setName("secure_payment");
        StartEvent startEvent = new StartEvent();

        ProcessSequenceFlow startToValidationSequenceFlow = new ProcessSequenceFlow();

        ServiceTask validationServiceTask = new ServiceTask();
        validationServiceTask.setImplementationType("java");
        validationServiceTask.setImplementation("basicValidation");

        startEvent.getOutcomingFlowList().add(startToValidationSequenceFlow);
        startToValidationSequenceFlow.setSourceActivity(startEvent);
        startToValidationSequenceFlow.setTargetActivity(validationServiceTask);
        validationServiceTask.getIncomingFlowList().add(startToValidationSequenceFlow);

        ProcessSequenceFlow validationToAdapterSequenceFlow = new ProcessSequenceFlow();

        ServiceTask adapterServiceTask = new ServiceTask();
        adapterServiceTask.setImplementationType("java");
        adapterServiceTask.setImplementation("adpaterMhtOrder");

        adapterServiceTask.getIncomingFlowList().add(validationToAdapterSequenceFlow);
        validationToAdapterSequenceFlow.setSourceActivity(validationServiceTask);
        validationToAdapterSequenceFlow.setTargetActivity(adapterServiceTask);

        ProcessSequenceFlow adapterToEndSequenceFlow = new ProcessSequenceFlow();
        EndEvent endEvent = new EndEvent();

        endEvent.getIncomingFlowList().add(adapterToEndSequenceFlow);
        adapterToEndSequenceFlow.setSourceActivity(adapterServiceTask);
        adapterToEndSequenceFlow.setTargetActivity(endEvent);

        processDefinition.getNodeList().add(startEvent);
        processDefinition.getNodeList().add(validationServiceTask);
        processDefinition.getNodeList().add(adapterServiceTask);
        processDefinition.getNodeList().add(endEvent);

    }
    
    @Test
    public void test(){
        
        
         
    }
    
    
    
}
