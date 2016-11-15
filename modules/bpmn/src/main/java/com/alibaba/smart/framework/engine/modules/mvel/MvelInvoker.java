package com.alibaba.smart.framework.engine.modules.mvel;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ettear on 16-4-29.
 */
public class MvelInvoker  {

    private static final Logger   LOGGER          = LoggerFactory.getLogger(MvelInvoker.class);

    
    private ConditionExpression              script;

    public MvelInvoker(ConditionExpression script) {
        // TODO  预编译
        this.script = script;
        // stmt = MVEL.compileExpression(script);
    }

//    @Override
//    public Message invoke(ExecutionContext context) {
//
//        try {
//            Object result = MVEL.eval(this.script.getExpressionContent(), context.getRequest());
//            LOGGER.debug("Invoke mvel '" + this.script + "' result is " + result);
//
//
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(),e);
//
//        }
//
//        //FIXME
//        return null;
//    }
}
