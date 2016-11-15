package com.alibaba.smart.framework.engine.pvm;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmProcessComponent {

    String getId();

    String getVersion();

    ClassLoader getClassLoader();

    PvmProcessDefinition getProcess();

    Map<String, PvmProcessDefinition> getProcesses();

    void addProcess(String id, PvmProcessDefinition process);
}
