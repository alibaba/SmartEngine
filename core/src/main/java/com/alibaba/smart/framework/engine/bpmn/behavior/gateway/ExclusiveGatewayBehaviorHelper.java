package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by 高海军 帝奇 74394 on  2020-09-21 18:12.
 */
public class ExclusiveGatewayBehaviorHelper {

    public static void chooseOnlyOne(ExecutionContext context, Map<String, PvmTransition> outcomeTransitions) {

        List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());

        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {

            PvmTransition pendingTransition = transitionEntry.getValue();
            boolean matched = pendingTransition.match(context);

            if (matched) {
                matchedTransitions.add(pendingTransition);
            }

        }

        if(0 == matchedTransitions.size()){
            throw new EngineException("No Transitions matched,please check input request and condition expression.");
        }


        if(1 != matchedTransitions.size()){
            throw new EngineException("Multiple Transitions matched: "+ matchedTransitions);
        }

        for (PvmTransition matchedPvmTransition : matchedTransitions) {
            PvmActivity target = matchedPvmTransition.getTarget();
            target.enter(context);
        }
    }
}