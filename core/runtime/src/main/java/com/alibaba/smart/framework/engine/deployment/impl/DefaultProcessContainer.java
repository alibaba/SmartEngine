package com.alibaba.smart.framework.engine.deployment.impl;

import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Process Container
 * Created by ettear on 16-4-19.
 */
public class DefaultProcessContainer implements ProcessContainer {
    private final static String DEFAULT_VERSION="1.0.0";
    private final static String DEFAULT_NAMESPACE="smart";

    //private Map<String, Map<String, RuntimeProcessComponent>> processes = new ConcurrentHashMap<>();
    private Map<String, RuntimeProcess> processes = new ConcurrentHashMap<>();
    private String namespace=DEFAULT_NAMESPACE;

    @Override
    public void install(RuntimeProcessComponent processComponent) {
        String processId=processComponent.getId();
        String version=processComponent.getVersion();
        String uri=this.buildComponentUri(processId, version);
        //Add to process store
        if(null!=processComponent.getProcess()){
            this.install(uri,processComponent.getProcess());
        }
        if(null!=processComponent.getProcesses() && !processComponent.getProcesses().isEmpty()){
            for (Map.Entry<String, RuntimeProcess> processEntry : processComponent.getProcesses().entrySet()) {
                RuntimeProcess subProcess=processEntry.getValue();
                String subUri=uri+"/"+subProcess.getId();
                this.install(subUri,subProcess);
            }
        }
    }

    @Override
    public RuntimeProcess get(String processId, String version) {
        String uri=this.buildComponentUri(processId, version);
        return this.get(uri);
    }

    @Override
    public RuntimeProcess get(String uri) {
        return this.processes.get(uri);
    }

    /**
     * Install Process
     * @param uri Process URI
     * @param runtimeProcess Process
     */
    private void install(String uri,RuntimeProcess runtimeProcess){
        runtimeProcess.setUri(uri);
        this.processes.put(uri,runtimeProcess);
    }

    private String buildComponentUri(String processId, String version){
        StringBuilder uriBuilder=new StringBuilder("smart://").append(this.getNamespace()).append("/process/");
        uriBuilder.append(processId);
        if(StringUtils.isBlank(version)){
            uriBuilder.append("/").append(DEFAULT_VERSION);
        }else{
            uriBuilder.append("/").append(version);
        }
        return uriBuilder.toString();
    }

    //Getter & Setter

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
