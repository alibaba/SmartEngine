package com.alibaba.smart.framework.engine.context;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author ettear
 * Created by ettear on 2018/7/24.
 */
public interface ProcessContext {

    ProcessInstance getProcessInstance();

    void setProcessInstance(ProcessInstance processInstance);


}
