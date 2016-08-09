package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import com.alibaba.smart.framework.engine.pvm.PvmProcess;
import com.alibaba.smart.framework.engine.pvm.PvmProcessComponent;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeProcessComponent implements PvmProcessComponent {

    private String                  id;
    private String                  version;
    private ClassLoader             classLoader;
    private PvmProcess              process;
    private Map<String, PvmProcess> processes = new HashMap<>();

    @Override
    public void addProcess(String id, PvmProcess process) {
        this.processes.put(id, process);
    }
}
