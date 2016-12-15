package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.pvm.PvmProcessComponent;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ettear on 16-4-13.
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
