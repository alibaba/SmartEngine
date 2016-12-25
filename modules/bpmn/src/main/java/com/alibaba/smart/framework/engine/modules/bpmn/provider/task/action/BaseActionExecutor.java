package com.alibaba.smart.framework.engine.modules.bpmn.provider.task.action;

import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.util.ParamChecker;
import com.alibaba.smart.framework.engine.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * Created by dongdongzdd on 16/9/8.
 */
public abstract class BaseActionExecutor {

    private static final Logger logger = LoggerFactory.getLogger(BaseActionExecutor.class);


    /**
     * 目前仅支持传入一个map<String,Object>
     * @param obj
     * @param methodStr
     * @return
     * @throws Exception
     */
    protected Message executeMethod(Object obj, String methodStr, Map<String,Object> workflowContext)  {
        Message message = new DefaultMessage();
        if (workflowContext.isEmpty()) {
            message.setFault(true);
            message.setSuspend(true);
            return message;
        } else {
            @SuppressWarnings("rawtypes")
            Class[] clazzs = new Class[]{workflowContext.getClass()};
            try {
                Method method = ReflectUtil.getMethod(obj, methodStr, clazzs);
                ParamChecker.notNull(method,"spring method ");
                method.invoke(obj, workflowContext);
            } catch (ReflectiveOperationException e) {
                if (logger.isDebugEnabled()) {
                    logger.error("method is " + methodStr);
                    logger.error("obj is " + obj);
                    logger.error("fumo " , e);
                }
                message.setFault(true);
                message.setBody(e);
                message.setSuspend(true);
                return message;
            }
        }

        message.setFault(false);
        message.setSuspend(false);
        return message;
    }

    public abstract Message execute();



}
