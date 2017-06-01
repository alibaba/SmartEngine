package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.persister.PersisterStrategy;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.task.util.TccDelegationUtil;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;

public class EndEventBehavior extends AbstractActivityBehavior<EndEvent> implements ActivityBehavior<EndEvent> {

    public EndEventBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void buildInstanceRelationShip(PvmActivity pvmActivity, ExecutionContext executionContext) {
        ProcessInstance processInstance = executionContext.getProcessInstance();
        processInstance.setStatus(InstanceStatus.completed);
        processInstance.setCompleteDate(DateUtil.getCurrentDate());

        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, executionContext);

        processInstance.addNewActivityInstance(activityInstance);

        EndEvent endEvent = (EndEvent) pvmActivity.getModel();
        String className = endEvent.getClassName();
        executeExtension(executionContext, className);


    }

    @Override
    public boolean needSuspend(ExecutionContext context) {

        ProcessInstance processInstance = context.getProcessInstance();
        if(null != processInstance.getParentInstanceId()){
            //说明当前ProcessInstance是子流程实例,此时不能暂停,需要继续把父流程继续往后去驱动。


            return false;

        }
        return true;
    }

    @Override
    public void leave(PvmActivity runtimeActivity, ExecutionContext context){


        //子流程结束时,才会进入到该环节里面来。这个时候没要慌,需要找出父流程的执行实例id,然后继续执行父流程的后续节点。
        ProcessInstance processInstance = context.getProcessInstance();
        if(null !=  processInstance.getParentInstanceId()){
            Long parentExecutionInstanceId = processInstance.getParentExecutionInstanceId();


            //BE AWARE:这里重新驱动了父流程的流转,但是强烈依赖此时的线程上下文数据要变成父流程的。
            //BUT BUT BUT !!! 如果此时,子流程是全自动节点,但是父流程还没机会去完成实例化的话,下面的signal方法是拿不到数据的。

            PersisterStrategy persisterStrategy = context.getProcessEngineConfiguration().getPersisterStrategy();
            persisterStrategy.getProcessInstanceByExecutionInstanceId(parentExecutionInstanceId);

            SmartEngine smartEngine = context.getExtensionPointRegistry().getExtensionPoint(SmartEngine.class);
            ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();
            executionCommandService.signal(parentExecutionInstanceId);
        }

    }

    private void executeExtension(ExecutionContext executionContext, String className) {
        if (null == className) {

        } else {
            TccDelegationUtil.execute(executionContext, className);

        }
    }
}
