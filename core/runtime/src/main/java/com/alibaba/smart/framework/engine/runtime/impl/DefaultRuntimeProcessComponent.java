package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeProcessComponent implements RuntimeProcessComponent {

    private String         id;
    private String         version;
    private ClassLoader    classLoader;
    private RuntimeProcess process;
    private Map<String, RuntimeProcess> processes = new HashMap<>();

    @Override
    public void addProcess(String id, RuntimeProcess process) {
        this.processes.put(id,process);
    }
}
