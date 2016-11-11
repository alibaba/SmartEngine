package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import com.alibaba.smart.framework.engine.pvm.PvmProcessComponent;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
@Data
public class DefaultRuntimeProcessComponent implements PvmProcessComponent {

    private String                  id;
    private String                  version;
    private ClassLoader             classLoader;
    private PvmProcessDefinition              process;
    private Map<String, PvmProcessDefinition> processes = new HashMap<>();

    @Override
    public void addProcess(String id, PvmProcessDefinition process) {
        this.processes.put(id, process);
    }
}
