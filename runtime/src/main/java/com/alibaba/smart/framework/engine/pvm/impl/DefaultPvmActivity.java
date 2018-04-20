package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.NoSatisfyTargetInExclusiveGatewayException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.service.async.AsyncTaskExecutor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPvmActivity extends AbstractPvmActivity implements PvmActivity {

    protected ActivityInstanceFactory activityInstanceFactory;
    protected ExecutionInstanceFactory executionInstanceFactory;
    private ExecutePolicyBehavior executePolicyBehavior;
    //class full name
    private String type;

    public DefaultPvmActivity(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
    }

        @Override
    public void enter(ExecutionContext context) {
        this.buildInstanceRelationShip(context);
        this.executePolicyBehavior.enter(this,context);

        if (context.isNeedPause()) {

            //FIXME why ??
            context.setNeedPause(false);
            // break;
            return;
        }

            //TODO ettear 以下逻辑待迁移到ExecutionPolicy中去
            this.execute(context);
    }


    @Override
    public void execute(ExecutionContext context) {
        this.executePolicyBehavior.execute(this,context);

        if (context.isNeedPause()) {
            context.setNeedPause(false);
            // break;
            return;
        }
        //TODO ettear 以下逻辑待迁移到ExecutionPolicy中去
        this.invoke(PvmEventConstant.ACTIVITY_END.name(), context);
        this.executeRecursively(context);
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

    private void executeRecursively(ExecutionContext context) {

        //执行每个节点的hook方法
        Map<String, PvmTransition> outcomeTransitions = this.getOutcomeTransitions();

        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            Set<PvmTransition> matchedTransitions = new TreeSet<PvmTransition>();
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pendingTransition = transitionEntry.getValue();
                boolean matched = pendingTransition.match(context);

                if (matched) {
                    matchedTransitions.add(pendingTransition);
                }

            }
            //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。

           executeNext(matchedTransitions,context);

        }
    }

    private void executeNext(Set<PvmTransition> transitions,ExecutionContext context){
        if("ExclusiveGateway".equals(this.type)){
            if(transitions.isEmpty()){
                executeForExclusiveGateway(null,context);
            }else {
                for (PvmTransition transition : transitions) {
                    executeForExclusiveGateway(transition, context);
                    break;
                }
            }
        }else if("InclusiveGateway".equals(this.type)){
            executeForInclusiveGateway(transitions,context);
        }else{
            executeInNormalWay(transitions,context);
        }
    }

    private void executeForExclusiveGateway(PvmTransition transition,ExecutionContext context) {
        if(transition == null){
            throw new NoSatisfyTargetInExclusiveGatewayException();
        }
        transition.execute(context);
    }

    private void executeForInclusiveGateway(Set<PvmTransition> transitions,ExecutionContext context){
        if(sync){
            for(PvmTransition transition : transitions){
                transition.execute(context);
            }
        }else{
            for (PvmTransition transition : transitions) {
                AsyncTaskExecutor.submit(transition,context);
            }
        }
    }

    private void executeInNormalWay(Set<PvmTransition> transitions,ExecutionContext context){
        for(PvmTransition transition : transitions){
            transition.execute(context);
        }
    }

    @Override
    public String toString() {
        return " [id=" + getModel().getId() + "]";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

}
