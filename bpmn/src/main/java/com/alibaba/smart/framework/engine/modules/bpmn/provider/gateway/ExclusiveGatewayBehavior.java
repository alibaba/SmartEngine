package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity.CallActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR,binding = ExclusiveGateway.class)

public class ExclusiveGatewayBehavior extends AbstractActivityBehavior<ExclusiveGateway> {
    //
    //public ExclusiveGatewayBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
    //    super(extensionPointRegistry, runtimeActivity);
    //}

    public ExclusiveGatewayBehavior() {
        super();
    }


    @Override
    public void leave(PvmActivity pvmActivity, ExecutionContext context) {

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
                throw new EngineException("outcomeTransitions size shoud >= 2");
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
