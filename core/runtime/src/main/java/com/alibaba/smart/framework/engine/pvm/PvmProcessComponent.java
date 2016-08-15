package com.alibaba.smart.framework.engine.pvm;

import java.util.Map;

/**
 * Created by ettear on 16-4-13.
 */
// TODO ettear 命名
public interface PvmProcessComponent {

    String getId();

    String getVersion();

    ClassLoader getClassLoader();

    PvmProcessDefinition getProcess();

    Map<String, PvmProcessDefinition> getProcesses();

    void addProcess(String id, PvmProcessDefinition process);
}
