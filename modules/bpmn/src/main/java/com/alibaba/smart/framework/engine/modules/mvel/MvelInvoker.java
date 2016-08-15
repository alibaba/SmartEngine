package com.alibaba.smart.framework.engine.modules.mvel;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;

/**
 * Created by ettear on 16-4-29.
 */
public class MvelInvoker implements Invoker {

    private static final Logger   LOGGER          = LoggerFactory.getLogger(MvelInvoker.class);

    
    private ConditionExpression              script;

    public MvelInvoker(ConditionExpression script) {
        // TODO ettear 预编译
        this.script = script;
        // stmt = MVEL.compileExpression(script);
    }

    @Override
    public Message invoke(ExecutionContext context) {
        Message message = new DefaultMessage();
//        Map<String, Object> mvelContext = new HashMap<>();
////        InstanceFact current = context.getCurrentExecution().getFact();
////        mvelContext.put("fact", context.getProcessInstance().getFact());
////        mvelContext.put("current", current);
////        mvelContext.putAll(current);
        try {
            Object result = MVEL.eval(this.script.getExpressionContent(), context.getRequest());
            LOGGER.debug("Invoke mvel '" + this.script + "' result is " + result);

            message.setBody(result);
 
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);

            message.setFault(true);
        }

        return message;
    }
}
