//package com.alibaba.smart.framework.process.behavior.event;
//
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
//import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
//import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
//import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
//import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
//import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
//import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
//import com.alibaba.smart.framework.process.behavior.AbstractActivityBehavior;
//
///**
// * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
// */
//public class StartEventBehavior extends AbstractActivityBehavior {
//
//    @Override
//    public void execute(ExecutionContext executionContext) {
//
//        // TODO 这块数据拼接职责的确有点太businessLogic,考虑拆分下
//
//        ExtensionPointRegistry extensionPointRegistry = executionContext.getExtensionPointRegistry();
//        ProcessInstanceFactory  processInstanceFactory= extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class) ;
//        ExecutionInstanceFactory  executionInstanceFactory= extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class) ;
//        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
//
//
//        ProcessInstance processInstance = processInstanceFactory.create();
//        ExecutionInstance executionInstance = executionInstanceFactory.create();
//        executionInstance.setProcessInstanceId(processInstance.getInstanceId());
////        processInstance.addExecution(executionInstance);
//
//        String processInstanceId = processInstance.getInstanceId();
//        processInstance.setStatus(InstanceStatus.running);
//
//        ActivityInstance activityInstance = activityInstanceFactory.create();
//        activityInstance.setProcessInstanceId(processInstanceId);
//
//
//        //FIXME
//
//        super.activityBehaviorUtil.leaveCurrentActivity();
//    }
//
//    @Override
//    public void signal() {
//        // TODO add custom exception
//        throw new RuntimeException("this activity doesn't accept signals");
//
//    }
//
//}
