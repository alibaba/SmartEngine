package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import java.util.Map;


import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;

public class EndEventBehavior extends AbstractActivityBehavior<EndEvent> {
    private ExecutionCommandService executionCommandService;

    public EndEventBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
        this.executionCommandService = extensionPointRegistry.getExtensionPoint(ExecutionCommandService.class);
    }

    @Override
    public void leave(ExecutionContext context) {
        ProcessInstance processInstance = context.getProcessInstance();
        processInstance.setStatus(InstanceStatus.completed);
        processInstance.setCompleteTime(DateUtil.getCurrentDate());
        Map<String, Object> request = context.getRequest();

        if(null != request){
            Object taskInstanceTag = request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG);
            //processInstance的tag使用的是最后一个完成的任务的tag
            if (taskInstanceTag != null) {
                processInstance.setTag(taskInstanceTag.toString());
            }
        }



        //==== 子流程结束，执行父流程 ====
        //子流程结束时,才会进入到该环节里面来。需要找出父流程的执行实例id,然后继续执行父流程的后续节点。
        Long parentExecutionInstanceId = processInstance.getParentExecutionInstanceId();
        if (null != parentExecutionInstanceId) {
            //如果上下文中有父上下文，说明父子在同一线程中，父线程本身就会执行，所以不需要启动
            if (null == context.getParent()) {
                this.executionCommandService.signal(parentExecutionInstanceId);
            }
        }
    }
}
