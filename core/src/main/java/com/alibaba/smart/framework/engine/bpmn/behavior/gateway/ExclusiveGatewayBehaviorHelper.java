package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by 高海军 帝奇 74394 on  2020-09-21 18:12.
 */
public class ExclusiveGatewayBehaviorHelper {

    private static final String DEFAULT = "default";

    public static void chooseOnlyOne(PvmActivity pvmActivity , ExecutionContext context, Map<String, PvmTransition> outcomeTransitions) {

        String processDefinitionActivityId = pvmActivity.getModel().getId();

        List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());

        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {

            PvmTransition pendingTransition = transitionEntry.getValue();
            boolean matched = pendingTransition.match(context);

            if (matched) {
                matchedTransitions.add(pendingTransition);
            }

        }

        //如果都没匹配到,就使用DefaultSequenceFlow
        if(0 == matchedTransitions.size()){

            Map<String, String> properties = pvmActivity.getModel().getProperties();
            if(MapUtil.isNotEmpty(properties)){
                String defaultSeqFLowId = properties.get(DEFAULT);
                if(StringUtil.isNotEmpty(defaultSeqFLowId)){
                    PvmTransition pvmTransition = outcomeTransitions.get(defaultSeqFLowId);
                    if (null != pvmTransition){
                        matchedTransitions.add(pvmTransition);
                    }else {
                        throw new EngineException("No default sequence flow found,check activity id :"+processDefinitionActivityId);
                    }
                }else{
                    // do nothing
                }

            }else{
                throw new EngineException("properties can not be empty,  check activity id :"+processDefinitionActivityId);

            }


        }

        if(0 == matchedTransitions.size()){
            throw new EngineException("No Transitions matched,please check input request and condition expression,activity id :"+processDefinitionActivityId);
        }


        if(1 != matchedTransitions.size()){
            throw new EngineException("Multiple Transitions matched: "+ matchedTransitions+" ,check activity id :"+processDefinitionActivityId);
        }

        for (PvmTransition matchedPvmTransition : matchedTransitions) {
            PvmActivity target = matchedPvmTransition.getTarget();
            target.enter(context);
        }
    }
}