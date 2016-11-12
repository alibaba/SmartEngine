package com.alibaba.smart.framework.process.behavior.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.process.behavior.AbstractActivityBehavior;
import com.alibaba.smart.framework.process.session.ExecutionSession;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
public class UserTaskBehavior extends AbstractActivityBehavior {

    @Override
    public void execute(ExecutionContext executionContext) {
        // 创建 ai,ei和user task instance

        ExecutionSession executionSession =null;//  ThreadLocalUtil.get();
        ProcessInstance processInstance = executionSession.getProcessInstance();

        ExtensionPointRegistry extensionPointRegistry = executionContext.getExtensionPointRegistry();

        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);

        ExecutionInstanceFactory executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);

        ExecutionInstance executionInstance = executionInstanceFactory.create();
        executionInstance.setInstanceId(InstanceIdUtil.uuid());
        executionInstance.setProcessInstanceId(processInstance.getInstanceId());
        processInstance.addExecution(executionInstance);// 执行实例添加到流程实例

        ActivityInstance activityInstance = activityInstanceFactory.create();

        // TODO 对数据库索引的友好,增加activityInstance,execution 不变
        activityInstance.setInstanceId(InstanceIdUtil.uuid());

        // TODO 考虑删除,在持久层最终完成赋值,因为之间已经有关联关系了
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());

        activityInstance.setActivityId(executionSession.getCurrentRuntimeActivity().getModel().getId());

        executionInstance.setActivity(activityInstance);

        processInstance.addActivityInstance(activityInstance);
    }

    @Override
    public void signal() {
        // 更新 ai, 删除ei,跟新 user task instance

        // TODO 设置 ExecutionSession executionSession = ThreadLocalExecutionSessionUtil.get();

        super.activityBehaviorUtil.leaveCurrentActivity();

    }
}
