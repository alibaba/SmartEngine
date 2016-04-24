package com.alibaba.smart.framework.process.behavior.event;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;
import com.alibaba.smart.framework.process.behavior.AbstractActivityBehavior;
import com.alibaba.smart.framework.process.context.ProcessContext;
import com.alibaba.smart.framework.process.context.ProcessContextHolder;
import com.alibaba.smart.framework.process.session.ExecutionSession;
import com.alibaba.smart.framework.process.session.util.ThreadLocalExecutionSessionUtil;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
public class StartEventBehavior extends AbstractActivityBehavior{

    @Override
    public void execute() {
        //TODO 每个节点有自己的行为,每个行为负责自己的职责.具体Execution生成几个,由网关或者默认行为控制.
        
        //TODO 这块数据拼接职责的确有点太businessLogic,考虑拆分下
        
        ProcessContext processContext = ProcessContextHolder.get();
        
        //从扩展注册机获取实例工厂
        ExtensionPointRegistry extensionPointRegistry = processContext.getProcessEngine().getExtensionPointRegistry();
//        ExecutionInstanceFactory executionInstanceFactory = extensionPointRegistry.getExtensionPoint(
//                ExecutionInstanceFactory.class);
        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(
                ActivityInstanceFactory.class);
        
        ProcessInstanceFactory processInstanceFactory = extensionPointRegistry.getExtensionPoint(
                                                                                                   ProcessInstanceFactory.class);
        

        //流程实例ID
        ProcessInstance processInstance = processInstanceFactory.create();
        String processInstanceId = processInstance.getInstanceId();
        //状态
        processInstance.setStatus(InstanceStatus.running);
        
        //构建活动实例: 指向开始节点
        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setInstanceId(InstanceIdUtils.uuid());
        activityInstance.setProcessInstanceId(processInstanceId);
        
        ExecutionSession executionSession = ThreadLocalExecutionSessionUtil.get();
        activityInstance.setActivityId(executionSession.getCurrentRuntimeActivity().getId());
        processInstance.addActivityInstance(activityInstance);
        
        executionSession.setProcessInstance(processInstance);

      
        // TODO  这段逻辑要结合store 一起看下
        
        super.activityBehaviorUtil.leaveCurrentActivity();
    }

    @Override
    public void signal() {
        //TODO add custom exception
        throw new RuntimeException("this activity doesn't accept signals");

    }

}
