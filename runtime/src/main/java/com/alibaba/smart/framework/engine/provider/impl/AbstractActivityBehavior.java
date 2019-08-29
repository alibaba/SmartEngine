package com.alibaba.smart.framework.engine.provider.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivityBehavior<T extends Activity> implements ActivityBehavior {

    private PvmActivity pvmActivity;

    protected ExtensionPointRegistry extensionPointRegistry;

    protected ProcessInstanceFactory processInstanceFactory;
    protected ExecutionInstanceFactory executionInstanceFactory;
    protected ActivityInstanceFactory activityInstanceFactory;
    protected TaskInstanceFactory taskInstanceFactory;

    public AbstractActivityBehavior() {
        //FIXME
    }

    public AbstractActivityBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity pvmActivity) {
        this.pvmActivity = pvmActivity;
        this.extensionPointRegistry = extensionPointRegistry;
        this.processInstanceFactory = extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
        this.taskInstanceFactory = extensionPointRegistry.getExtensionPoint(TaskInstanceFactory.class);
    }

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }


    protected PvmActivity getPvmActivity() {
        return pvmActivity;
    }

    @Override
    public boolean enter(ExecutionContext context) {
        this.buildInstanceRelationShip(context);

        ActivityInstance activityInstance = context.getActivityInstance();

        ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);

        List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(1);
        executionInstanceList.add(executionInstance);
        activityInstance.setExecutionInstanceList(executionInstanceList);
        context.setExecutionInstance(executionInstance);

        pvmActivity.invoke(PvmEventConstant.ACTIVITY_START.name(), context);
        if (context.isNeedPause()) {
            executionInstance.setStatus(InstanceStatus.suspended);
        }

        return false;
    }

    private void buildInstanceRelationShip(ExecutionContext context){
        ProcessInstance processInstance = context.getProcessInstance();

        ActivityInstance activityInstance = this.activityInstanceFactory.create(this.getModel(), context);
        //ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,  context);
        //
        //activityInstance.setExecutionInstance(executionInstance);
        processInstance.addActivityInstance(activityInstance);
        //
        //context.setExecutionInstance(executionInstance);
        context.setActivityInstance(activityInstance);
    }

    @Override
    public boolean execute(ExecutionContext context) {
        return false;
    }

    @Override
    public void leave(PvmActivity pvmActivity,ExecutionContext context) {

        //执行每个节点的hook方法
        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();

        if(MapUtil.isEmpty(outcomeTransitions)){

            //TUNE log debug ,more log
            return;
        }else{

            if( outcomeTransitions.size() ==1){
                for (Entry<String, PvmTransition> pvmTransitionEntry : outcomeTransitions.entrySet()) {
                    PvmActivity target = pvmTransitionEntry.getValue().getTarget();
                    target.enter(context);
                }
            }else {
                //TUNE default throw exception，exclude gateway behavior
            }
        }

        //if (isNotEmpty(outcomeTransitions)) {
        //    List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());
        //    for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
        //        PvmTransition pendingTransition = transitionEntry.getValue();
        //        boolean matched = pendingTransition.match(context);
        //
        //        if (matched) {
        //            matchedTransitions.add(pendingTransition);
        //        }
        //
        //    }
        //    //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。
        //
        //    for (PvmTransition matchedPvmTransition : matchedTransitions) {
        //        matchedPvmTransition.execute(context);
        //    }
        //}

    }

    private boolean isNotEmpty(Map<String, PvmTransition> outcomeTransitions) {
        return null != outcomeTransitions && !outcomeTransitions.isEmpty();
    }

    //private void executeRecursively(ExecutionContext context) {
    //
    //    //执行每个节点的hook方法
    //    Map<String, PvmTransition> outcomeTransitions = this.getOutcomeTransitions();
    //
    //    if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
    //        List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());
    //        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
    //            PvmTransition pendingTransition = transitionEntry.getValue();
    //            boolean matched = pendingTransition.match(context);
    //
    //            if (matched) {
    //                matchedTransitions.add(pendingTransition);
    //            }
    //
    //        }
    //        //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。
    //
    //        for (PvmTransition matchedPvmTransition : matchedTransitions) {
    //            matchedPvmTransition.execute(context);
    //        }
    //    }
    //}


    protected T getModel() {
        return (T)this.pvmActivity.getModel();
    }
}
