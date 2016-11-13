package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
public class DefaultPvmProcessInstance implements PvmProcessInstance {

    @Override
    public ProcessInstance start(ExecutionContext executionContext) {

        PvmProcessDefinition pvmProcessDefinition = executionContext.getPvmProcessDefinition();
        PvmActivity startActivity = pvmProcessDefinition.getStartActivity();
        this.executeCurrentActivityAndLookupNextTransitionRecursively(startActivity, executionContext);

        return executionContext.getProcessInstance();

    }


    private void executeCurrentActivityAndLookupNextTransitionRecursively(PvmActivity pvmActivity, ExecutionContext context) {

        //重要: 执行当前节点,会触发当前节点的行为执行
        pvmActivity.execute(context);

        if (context.isNeedPause()) {
            // break;
            return;
        }

        leaveCurrentActivity(pvmActivity, context);


    }

    private void leaveCurrentActivity(PvmActivity pvmActivity, ExecutionContext context) {
        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();

        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<PvmTransition> matchedTransitions = new ArrayList<>(outcomeTransitions.size());
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pvmTransition1 = transitionEntry.getValue();
                TransitionProvider invokerProvider = (TransitionProvider) pvmTransition1.getProvider();
                boolean matched = invokerProvider.execute(pvmTransition1, context);

                if (matched) {
                    matchedTransitions.add(pvmTransition1);
                }

            }
            //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。

            for (PvmTransition matchedTransition : matchedTransitions) {
                PvmActivity  targetPvmActivity =   matchedTransition.getTarget();
                this.executeCurrentActivityAndLookupNextTransitionRecursively(targetPvmActivity, context);
            }


        }
    }

    @Override
    public ProcessInstance signal(PvmActivity pvmActivity,ExecutionContext executionContext) {

        this.leaveCurrentActivity(pvmActivity, executionContext);

        return executionContext.getProcessInstance();

    }

}
