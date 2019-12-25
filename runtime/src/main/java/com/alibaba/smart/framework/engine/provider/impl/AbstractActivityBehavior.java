package com.alibaba.smart.framework.engine.provider.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.delegation.BehaviorUtil;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivityBehavior<T extends Activity> implements ActivityBehavior {

    @Setter
    private PvmActivity pvmActivity;

    @Setter
    @Getter
    protected ExtensionPointRegistry extensionPointRegistry;

    @Setter
    protected ProcessInstanceFactory processInstanceFactory;
    @Setter
    protected ExecutionInstanceFactory executionInstanceFactory;
    @Setter
    protected ActivityInstanceFactory activityInstanceFactory;
    @Setter
    protected TaskInstanceFactory taskInstanceFactory;
    @Setter
    protected ProcessEngineConfiguration processEngineConfiguration;
    @Setter
    protected ExecutionInstanceStorage executionInstanceStorage;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractActivityBehavior.class);

    public AbstractActivityBehavior() {
        //FIXME
    }



    protected PvmActivity getPvmActivity() {
        return pvmActivity;
    }

    @Override
    public boolean enter(ExecutionContext context) {

        ActivityInstance activityInstance = createSingleActivityInstance(context);

        ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);
        List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(1);
        executionInstanceList.add(executionInstance);

        activityInstance.setExecutionInstanceList(executionInstanceList);
        context.setExecutionInstance(executionInstance);

        if (context.isNeedPause()) {
            executionInstance.setStatus(InstanceStatus.suspended);
        }

        return false;
    }

    protected ActivityInstance createSingleActivityInstance(ExecutionContext context) {
        ProcessInstance processInstance = context.getProcessInstance();

        ActivityInstance activityInstance = this.activityInstanceFactory.create(this.getModel(), context);
        processInstance.addActivityInstance(activityInstance);
        context.setActivityInstance(activityInstance);
        return activityInstance;
    }

    //private void buildInstanceRelationShip(ExecutionContext context){
    //
    //}

    @Override
    public void execute(ExecutionContext context) {
        makeExtensionWorkAndExecuteBehavior(context);

        commonUpdateExecutionInstance(context);

    }

    protected void commonUpdateExecutionInstance(ExecutionContext context) {
        if (!context.isNeedPause()) {
            ExecutionInstance executionInstance = context.getExecutionInstance();
            MarkDoneUtil.markDoneExecutionInstance(executionInstance,executionInstanceStorage,
                processEngineConfiguration);
        }else{
            ExecutionInstance executionInstance = context.getExecutionInstance();
            executionInstance.setStatus(InstanceStatus.suspended);
        }
    }

    protected void makeExtensionWorkAndExecuteBehavior(ExecutionContext context) {
        T model = this.getModel();

        Map<String,String>  properties = model.getProperties();
        BehaviorUtil.executionBehaviorIf(context, properties,this.extensionPointRegistry,this.pvmActivity);
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
