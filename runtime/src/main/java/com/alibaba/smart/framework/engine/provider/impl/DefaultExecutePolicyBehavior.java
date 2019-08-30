//package com.alibaba.smart.framework.engine.provider.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.alibaba.smart.framework.engine.SmartEngine;
//import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
//import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
//import com.alibaba.smart.framework.engine.context.ExecutionContext;
//import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
//import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
//import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
//import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
//import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
//import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
//import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
//import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
//import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
//import com.alibaba.smart.framework.engine.pvm.PvmActivity;
//import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
//
///**
// * @author ettear
// * Created by ettear on 14/10/2017.
// */
//public class DefaultExecutePolicyBehavior implements ExecutePolicyBehavior, LifeCycleHook {
//    private ExtensionPointRegistry extensionPointRegistry;
//    private ExecutionInstanceFactory executionInstanceFactory;
//    private ExecutionInstanceStorage executionInstanceStorage;
//    private ProcessEngineConfiguration processEngineConfiguration;
//
//    public DefaultExecutePolicyBehavior(ExtensionPointRegistry extensionPointRegistry) {
//        this.extensionPointRegistry = extensionPointRegistry;
//    }
//
//    @Override
//    public void start() {
//        this.processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
//            SmartEngine.class).getProcessEngineConfiguration();
//
//        this.executionInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
//        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
//        this.executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
//
//    }
//
//    @Override
//    public void stop() {
//
//    }
//
//    @Override
//    public void enter(PvmActivity pvmActivity, ExecutionContext context) {
//
//        ActivityInstance activityInstance = context.getActivityInstance();
//
//        ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);
//
//        List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(1);
//        executionInstanceList.add(executionInstance);
//        activityInstance.setExecutionInstanceList(executionInstanceList);
//        context.setExecutionInstance(executionInstance);
//
//        pvmActivity.invoke(PvmEventConstant.ACTIVITY_START.name(), context);
//        if (context.isNeedPause()) {
//            executionInstance.setStatus(InstanceStatus.suspended);
//        }
//
//    }
//
//    @Override
//    public void execute(PvmActivity pvmActivity, ExecutionContext context) {
//
//        pvmActivity.invoke(PvmEventConstant.ACTIVITY_EXECUTE.name(), context);
//        if (!context.isNeedPause()) {
//            ExecutionInstance executionInstance = context.getExecutionInstance();
//            MarkDoneUtil.markDoneExecutionInstance(executionInstance,executionInstanceStorage,
//                processEngineConfiguration);
//        }else{
//            ExecutionInstance executionInstance = context.getExecutionInstance();
//            executionInstance.setStatus(InstanceStatus.suspended);
//        }
//    }
//
//}
