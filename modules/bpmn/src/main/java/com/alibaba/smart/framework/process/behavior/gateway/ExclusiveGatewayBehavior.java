package com.alibaba.smart.framework.process.behavior.gateway;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;
import com.alibaba.smart.framework.process.behavior.AbstractActivityBehavior;
import com.alibaba.smart.framework.process.context.ProcessContext;
import com.alibaba.smart.framework.process.context.ProcessContextHolder;
import com.alibaba.smart.framework.process.session.ExecutionSession;
import com.alibaba.smart.framework.process.session.util.ThreadLocalExecutionSessionUtil;


public class ExclusiveGatewayBehavior  extends AbstractActivityBehavior{
    @Override
    public void execute() {
        ProcessContext processContext = ProcessContextHolder.get();
        ExecutionSession executionSession = ThreadLocalExecutionSessionUtil.get();

        
        ExtensionPointRegistry extensionPointRegistry = processContext.getProcessEngine().getExtensionPointRegistry();

        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(
                                                                                                   ActivityInstanceFactory.class);
        
        ActivityInstance activityInstance = activityInstanceFactory.create();
        
        //TODO 对数据库索引的友好
        activityInstance.setInstanceId(InstanceIdUtils.uuid());
        
        ProcessInstance processInstance =  executionSession.getProcessInstance();
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());
        
        activityInstance.setActivityId(executionSession.getCurrentRuntimeActivity().getId());
        
        //FIXME excution 与activiy 的关系, 感觉没有必然联系,因为到落库时,只有一个活跃的userTask.  之前的execution 没啥含义.
//        processInstance.get
        
        super.activityBehaviorUtil.leaveCurrentActivity();
    }

    @Override
    public void signal() {
        
        //TODO add custom exception
        throw new RuntimeException("this activity doesn't accept signals");        
    }
}
