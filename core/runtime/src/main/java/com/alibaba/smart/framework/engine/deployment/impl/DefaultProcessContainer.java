package com.alibaba.smart.framework.engine.deployment.impl;

import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Process Container
 * Created by ettear on 16-4-19.
 */
public class DefaultProcessContainer implements ProcessContainer {

    private Map<String, Map<String, RuntimeProcessComponent>> processes = new ConcurrentHashMap<>();

    @Override
    public void add(RuntimeProcessComponent processComponent) {
        String processId=processComponent.getId();
        String version=processComponent.getVersion();
        //Add to process store
        Map<String, RuntimeProcessComponent> processVersions;
        if (this.processes.containsKey(processId)) {
            processVersions = this.processes.get(processId);
        } else {
            processVersions = new ConcurrentHashMap<>();
            this.processes.put(processId, processVersions);
        }
        if (!processVersions.containsKey(version)) {
            processVersions.put(version, processComponent);
        }
    }

    @Override
    public RuntimeProcessComponent get(String processId, String version) {
        Map<String, RuntimeProcessComponent> processVersions = this.processes.get(processId);
        if (null != processVersions && processVersions.containsKey(version)) {
            return processVersions.get(version);
        }
        return null;
    }
}
