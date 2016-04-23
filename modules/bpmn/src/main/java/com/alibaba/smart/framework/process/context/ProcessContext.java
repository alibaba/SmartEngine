package com.alibaba.smart.framework.process.context;

import lombok.Data;

import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM 
 * TODO 和底层的关系,抽象出接口?
 */

@Data
public class ProcessContext {

    private ProcessContainer processContainer;

    public RuntimeProcess get(String processDefinitionId, String version) {
        return this.processContainer.get(processDefinitionId, version);
    }

}
