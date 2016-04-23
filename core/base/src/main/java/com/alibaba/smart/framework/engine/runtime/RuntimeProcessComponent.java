package com.alibaba.smart.framework.engine.runtime;

import java.util.Map;

/**
 * Created by ettear on 16-4-13.
 */
// TODO ettear 命名
public interface RuntimeProcessComponent {

    String getId();

    String getVersion();

    ClassLoader getClassLoader();

    RuntimeProcess getProcess();

    Map<String, RuntimeProcess> getProcesses();

    void addProcess(String id, RuntimeProcess process);
}
