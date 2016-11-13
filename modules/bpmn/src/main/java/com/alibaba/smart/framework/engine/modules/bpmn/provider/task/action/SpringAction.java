package com.alibaba.smart.framework.engine.modules.bpmn.provider.task.action;


import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.util.ParamChecker;

import java.util.Map;

/**
 *
 * Created by dongdongzdd on 16/9/8.
 */
public class SpringAction extends BaseActionExecutor {

    private Object bean;
    private String excuteMethod;
    private Map<String, Object> context;

    public SpringAction(String obj, String method, Map<String, Object> workflowContext) {

        //FIXME
//        bean = ThreadLocalUtil.get().getBean(obj);
        excuteMethod = method;
        context = workflowContext;
    }


    @Override
    public Message execute() {
        ParamChecker.notNull(bean,"bean is null,not exist");
        ParamChecker.notNull(excuteMethod,"excute method is null");
        ParamChecker.notNull(context,"excute context is null");
        return super.executeMethod(bean,excuteMethod,context);

    }



}
