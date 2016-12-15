package com.alibaba.smart.framework.engine.deployment.impl;

import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.pvm.PvmProcessComponent;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Process Container Created by ettear on 16-4-19.
 */
public class DefaultProcessDefinitionContainer implements ProcessDefinitionContainer {

    private final static String     DEFAULT_VERSION   = "1.0.0";
    private final static String     DEFAULT_NAMESPACE = "smart";

    // private Map<String, Map<String, RuntimeProcessComponent>> processes = new ConcurrentHashMap<>();
    private Map<String, PvmProcessDefinition> processes         = new ConcurrentHashMap<>();
    private String                  namespace         = DEFAULT_NAMESPACE;

    @Override
    public void install(PvmProcessComponent processComponent) {
        String processDefinitionId = processComponent.getId();
        String version = processComponent.getVersion();
        String uri = this.buildComponentUri(processDefinitionId, version);
        // Add to process store
        if (null != processComponent.getProcess()) {
            this.install(uri, processComponent.getProcess());
        }
        if (null != processComponent.getProcesses() && !processComponent.getProcesses().isEmpty()) {
            for (Map.Entry<String, PvmProcessDefinition> processEntry : processComponent.getProcesses().entrySet()) {
                PvmProcessDefinition subProcess = processEntry.getValue();
                String subUri = uri + "/" + subProcess.getModel().getId();
                this.install(subUri, subProcess);
            }
        }
    }

    @Override
    public PvmProcessDefinition get(String processDefinitionId, String version) {
        String uri = this.buildComponentUri(processDefinitionId, version);
        return this.get(uri);
    }




    @Override
    public PvmProcessDefinition get(String uri) {
        return this.processes.get(uri);
    }

    /**
     * Install Process
     * 
     * @param uri Process URI
     * @param runtimeProcess Process
     */
    private void install(String uri, PvmProcessDefinition runtimeProcess) {
        runtimeProcess.setUri(uri);
        this.processes.put(uri, runtimeProcess);
    }

    private String buildComponentUri(String processDefinitionId, String version) {
        StringBuilder uriBuilder = new StringBuilder("smart://").append(this.getNamespace()).append("/process/");
        uriBuilder.append(processDefinitionId);
        if (StringUtils.isBlank(version)) {
            uriBuilder.append("/").append(DEFAULT_VERSION);
        } else {
            uriBuilder.append("/").append(version);
        }
        return uriBuilder.toString();
    }

    // Getter & Setter

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
