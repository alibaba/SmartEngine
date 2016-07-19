package com.alibaba.smart.framework.engine.modules.mvel;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

import com.alibaba.smart.framework.engine.assembly.Script;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;

/**
 * Created by ettear on 16-4-29.
 */
public class MvelInvoker implements Invoker {

//    private ExecutableStatement stmt;
    private Script              script;

    public MvelInvoker(Script script) {
        // TODO ettear 预编译
        this.script = script;
        // stmt = MVEL.compileExpression(script);
    }

    @Override
    public Message invoke(InstanceContext context) {
        Message message = new DefaultMessage();
        Map<String, Object> mvelContext = new HashMap<>();
//        InstanceFact current = context.getCurrentExecution().getFact();
//        mvelContext.put("fact", context.getProcessInstance().getFact());
//        mvelContext.put("current", current);
//        mvelContext.putAll(current);
        try {
            Object result = MVEL.eval(this.script.getContent(), mvelContext);
            message.setBody(result);
            String resultVariable = this.script.getResultVariable();
            if (null != resultVariable && !"".equals(resultVariable)) {
//                current.put(resultVariable, result);
            }
        } catch (Exception e) {
            message.setFault(true);
        }

        System.out.println("Invoke mvel '" + this.script + "' result is " + message.getBody());
        return message;
    }
}
