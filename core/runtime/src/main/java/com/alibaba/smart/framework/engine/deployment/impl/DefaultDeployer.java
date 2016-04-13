package com.alibaba.smart.framework.engine.deployment.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.assembly.SequenceFlow;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.definition.ProcessDefinition;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.exception.DeployException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.ProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.provider.ProviderFactory;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProvider;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeProcessComponent;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeSequenceFlow;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ettear on 16-4-13.
 */
public class DefaultDeployer implements Deployer, LifeCycleListener {

    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry        extensionPointRegistry;
    private SmartEngine smartEngine;
    private ProcessorExtensionPoint       processorExtensionPoint;
    private ProviderFactoryExtensionPoint providerFactoryExtensionPoint;
    private Map<String, Map<String, RuntimeProcessComponent>> processes = new ConcurrentHashMap<>();

    public DefaultDeployer(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public RuntimeProcessComponent deploy(String moduleName,String uri) throws DeployException{
        ClassLoader classLoader=this.smartEngine.getClassLoader(moduleName);//Find class loader
        if(null==classLoader){
            throw new DeployException("Module["+moduleName+"] not found!");
        }

        ProcessDefinition definition=this.load(classLoader,uri);


        return install(classLoader,definition);
    }

    @Override
    public RuntimeProcessComponent getProcess(String processId, String version) {
        Map<String, RuntimeProcessComponent> processVersions = this.processes.get(processId);
        if (null != processVersions && processVersions.containsKey(version)) {
            return processVersions.get(version);
        }
        return null;
    }

    @Override
    public void start() {
        this.smartEngine=this.extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        this.processorExtensionPoint = this.extensionPointRegistry.getExtensionPoint(ProcessorExtensionPoint.class);
        this.providerFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
                ProviderFactoryExtensionPoint.class);
    }

    @Override
    public void stop() {

    }

    private ProcessDefinition load(ClassLoader classLoader,String uri) throws DeployException{
        //Read xml file
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in = classLoader.getResourceAsStream(uri);
        XMLStreamReader reader;
        try {
            reader = factory.createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            throw new DeployException("Load process config file["+uri+"] failure!",e);
        }

        //Assembly: process xml file
        ProcessorContext context=new ProcessorContext();
        ProcessDefinition definition;
        try {
            return (ProcessDefinition)this.processorExtensionPoint.read(reader,context);
        } catch (ProcessorReadException | XMLStreamException e) {
            throw new DeployException("Read process config file["+uri+"] failure!",e);
        }
    }

    private RuntimeProcessComponent install(ClassLoader classLoader,ProcessDefinition definition){
        //Check
        if(null==definition){
            return null;
        }

        String processId=definition.getId();
        String version=definition.getVersion();

        //Build runtime model;
        DefaultRuntimeProcessComponent processComponent=new DefaultRuntimeProcessComponent();
        processComponent.setId(processId);
        processComponent.setVersion(version);
        processComponent.setClassLoader(classLoader);

        Process process=definition.getProcess();
        DefaultRuntimeProcess runtimeProcess=this.buildRuntimeProcess(process,classLoader);
        if(null!=runtimeProcess) {
            ActivityProviderFactory providerFactory=(ActivityProviderFactory)this.providerFactoryExtensionPoint.getProviderFactory(process.getClass());
            ActivityProvider activityProvider=providerFactory.createActivityProvider(runtimeProcess);
            runtimeProcess.setInvoker(activityProvider.createInvoker());
            processComponent.setProcess(runtimeProcess);
        }else{
            return null;
        }


        //Add to process store
        Map<String, RuntimeProcessComponent> processVersions;
        if(this.processes.containsKey(processId)){
            processVersions=this.processes.get(processId);
        }else{
            processVersions=new ConcurrentHashMap<>();
            this.processes.put(processId,processVersions);
        }
        if(!processVersions.containsKey(version)){
            processVersions.put(version,processComponent);
        }
        return processComponent;
    }

    private DefaultRuntimeProcess buildRuntimeProcess(Process process,ClassLoader classLoader){
        if(null==process){
            return null;
        }
        List<Base> elements=process.getElements();
        if(null==elements || elements.isEmpty()){
            return null;
        }

        //Build runtime model;
        DefaultRuntimeProcess runtimeProcess=new DefaultRuntimeProcess();
        runtimeProcess.setClassLoader(classLoader);
        runtimeProcess.setModelType(process.getClass());

        List<DefaultRuntimeSequenceFlow> runtimeSequenceFlows=new ArrayList<>();
        Map<String,DefaultRuntimeActivity> runtimeNodes=new HashMap<>();
        for (Base element : elements) {
            if(element instanceof Process){
                DefaultRuntimeProcess runtimeNode=this.buildRuntimeProcess((Process)element,classLoader);
                runtimeNodes.put(runtimeNode.getId(),runtimeNode);
            }else if(element instanceof SequenceFlow){
                DefaultRuntimeSequenceFlow runtimeSequenceFlow=new DefaultRuntimeSequenceFlow();

                SequenceFlow sequenceFlow=(SequenceFlow) element;
                runtimeSequenceFlow.setId(sequenceFlow.getId());
                runtimeSequenceFlow.setModelType(sequenceFlow.getClass());
                runtimeSequenceFlow.setSourceRef(sequenceFlow.getSourceRef());
                runtimeSequenceFlow.setTargetRef(sequenceFlow.getTargetRef());

                runtimeSequenceFlows.add(runtimeSequenceFlow);

            }else if(element instanceof Activity){
                DefaultRuntimeActivity runtimeActivity=new DefaultRuntimeActivity();
                Activity activity=(Activity) element;
                runtimeActivity.setId(activity.getId());
                runtimeActivity.setModelType(activity.getClass());

                runtimeNodes.put(runtimeActivity.getId(),runtimeActivity);
            }
        }

        for (DefaultRuntimeSequenceFlow runtimeSequenceFlow : runtimeSequenceFlows) {
            String sourceRef=runtimeSequenceFlow.getSourceRef();
            String targetRef=runtimeSequenceFlow.getTargetRef();
            DefaultRuntimeActivity source=runtimeNodes.get(sourceRef);
            DefaultRuntimeActivity target=runtimeNodes.get(targetRef);

            runtimeSequenceFlow.setSource(source);
            runtimeSequenceFlow.setTarget(target);
            source.addOutcomeSequenceFlows(runtimeSequenceFlow.getId(), runtimeSequenceFlow);
            target.addIncomeSequenceFlows(runtimeSequenceFlow.getId(),runtimeSequenceFlow);
        }

        for (DefaultRuntimeSequenceFlow runtimeSequenceFlow : runtimeSequenceFlows) {
            SequenceFlowProviderFactory providerFactory=(SequenceFlowProviderFactory)this.providerFactoryExtensionPoint.getProviderFactory(runtimeSequenceFlow.getModelType());
            SequenceFlowProvider sequenceFlowProvider=providerFactory.createSequenceFlowProvider(runtimeSequenceFlow);
            runtimeSequenceFlow.setInvoker(sequenceFlowProvider.createInvoker());
        }

        for (DefaultRuntimeActivity runtimeActivity : runtimeNodes.values()) {
            ActivityProviderFactory providerFactory=(ActivityProviderFactory)this.providerFactoryExtensionPoint.getProviderFactory(runtimeActivity.getModelType());
            ActivityProvider activityProvider=providerFactory.createActivityProvider(runtimeActivity);
            runtimeActivity.setInvoker(activityProvider.createInvoker());
        }

        return runtimeProcess;
    }
}
