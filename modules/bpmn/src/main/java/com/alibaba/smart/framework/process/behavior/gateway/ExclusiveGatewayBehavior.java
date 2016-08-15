package com.alibaba.smart.framework.process.behavior.gateway;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.util.ThreadLocalUtil;
import com.alibaba.smart.framework.process.behavior.AbstractActivityBehavior;
import com.alibaba.smart.framework.process.context.ProcessContext;
import com.alibaba.smart.framework.process.context.ProcessContextHolder;
import com.alibaba.smart.framework.process.session.ExecutionSession;

public class ExclusiveGatewayBehavior extends AbstractActivityBehavior {

    @Override
    public void execute() {
        ProcessContext processContext = ProcessContextHolder.get();
        ExecutionSession executionSession = null;// ThreadLocalUtil.get();

        ExtensionPointRegistry extensionPointRegistry = processContext.getProcessEngine().getExtensionPointRegistry();

        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);

        ActivityInstance activityInstance = activityInstanceFactory.create();

        // TODO 对数据库索引的友好,增加activityInstance,execution 不变
        activityInstance.setInstanceId(InstanceIdUtil.uuid());

        ProcessInstance processInstance = executionSession.getProcessInstance();

        // TODO 考虑删除,在持久层最终完成赋值,因为之间已经有关联关系了
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());

        activityInstance.setActivityId(executionSession.getCurrentRuntimeActivity().getId());

        processInstance.addActivityInstance(activityInstance);

        // FIXME excution 与activiy 的关系, 感觉没有必然联系,因为到落库时,只有一个活跃的userTask. 之前的execution 没啥含义.
        // processInstance.get

        super.activityBehaviorUtil.leaveCurrentActivity();
    }

    @Override
    public void signal() {

        // TODO add custom exception
        throw new RuntimeException("this activity doesn't accept signals");
    }
}
