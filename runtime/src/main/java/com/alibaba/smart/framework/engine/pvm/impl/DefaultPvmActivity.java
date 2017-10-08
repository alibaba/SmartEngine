package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

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

    public DefaultPvmActivity(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
    }

        @Override
    public void enter(ExecutionContext context) {
        this.buildInstanceRelationShip(context);
        this.invoke(PvmEventConstant.ACTIVITY_START.name(), context);
        if (context.isNeedPause()) {
            context.setNeedPause(false);
            // break;
            return;
        }
        execute(context);
    }


    @Override
    public void execute(ExecutionContext context) {
        this.invoke(PvmEventConstant.ACTIVITY_EXECUTE.name(), context);
        if (context.isNeedPause()) {
            context.setNeedPause(false);
            // break;
            return;
        }
        this.invoke(PvmEventConstant.ACTIVITY_END.name(), context);
        this.executeRecursively(context);
    }

    private void buildInstanceRelationShip(ExecutionContext context){
        //ProcessInstance processInstance = context.getProcessInstance();
        //
        //ActivityInstance activityInstance = this.activityInstanceFactory.create(this.getModel(), context);
        //ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,  context);
        //
        //activityInstance.setExecutionInstance(executionInstance);
        //processInstance.addNewActivityInstance(activityInstance);
        //
        //context.setExecutionInstance(executionInstance);
        //context.setActivityInstance(activityInstance);
    }

    private void executeRecursively(ExecutionContext context) {
        ExecutionInstance executionInstance=context.getExecutionInstance();

        MarkDoneUtil.markDone(executionInstance);

        //执行每个节点的hook方法
        Map<String, PvmTransition> outcomeTransitions = this.getOutcomeTransitions();

        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pendingTransition = transitionEntry.getValue();
                boolean matched = pendingTransition.match(context);

                if (matched) {
                    matchedTransitions.add(pendingTransition);
                }

            }
            //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。

            for (PvmTransition matchedTransition : matchedTransitions) {
                matchedTransition.execute(context);
            }
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
