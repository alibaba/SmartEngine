package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ExclusiveGateway.class)
public class ExclusiveGatewayBehavior extends AbstractActivityBehavior<ExclusiveGateway> {

    public ExclusiveGatewayBehavior() {
        super();
    }

    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {

        //执行每个节点的hook方法
        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();


            if( outcomeTransitions.size() >=2){

                List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());
                for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                    PvmTransition pendingTransition = transitionEntry.getValue();
                    boolean matched = pendingTransition.match(context);

                    if (matched) {
                        matchedTransitions.add(pendingTransition);
                    }

                }

                if(1 != matchedTransitions.size()){
                    throw new EngineException("Multiple Transitions matched: "+matchedTransitions);
                }

                for (PvmTransition matchedPvmTransition : matchedTransitions) {
                    PvmActivity target = matchedPvmTransition.getTarget();
                    target.enter(context);
                }


            }else {
                throw new EngineException("the outcomeTransitions.size() should >= 2");
            }
        }

}
