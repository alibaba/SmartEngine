package com.alibaba.smart.framework.process.context;

import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.process.engine.ProcessEngine;
import lombok.Data;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM TODO 和底层的关系,抽象出接口?
 */

@Data
public class ProcessContext {

    private ProcessEngine    processEngine;

    private ProcessDefinitionContainer processContainer;

    public PvmProcessDefinition get(String processDefinitionId, String version) {
        return this.processContainer.get(processDefinitionId, version);
    }

}