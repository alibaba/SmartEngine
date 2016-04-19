package com.alibaba.smart.framework.engine.deployment.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.assembly.SequenceFlow;
import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.deployment.exception.DeployException;
import com.alibaba.smart.framework.engine.extensibility.AssemblyProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProvider;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeProcessComponent;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeSequenceFlow;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * 默认部署器
 * Created by ettear on 16-4-13.
 */
public class DefaultDeployer implements Deployer, LifeCycleListener {

    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry          extensionPointRegistry;
    private SmartEngine                     smartEngine;
    private AssemblyProcessorExtensionPoint assemblyProcessorExtensionPoint;
    private ProviderFactoryExtensionPoint   providerFactoryExtensionPoint;
    private ProcessContainer                processContainer;

    public DefaultDeployer(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void deploy(String moduleName, String uri) throws DeployException {
        ClassLoader classLoader = this.smartEngine.getClassLoader(moduleName);//Find class loader
        if (null == classLoader) {
            throw new DeployException("Module[" + moduleName + "] not found!");
        }

        ProcessDefinition definition = this.load(classLoader, uri);
        RuntimeProcessComponent runtimeProcessComponent = install(classLoader, definition);
        if (null == runtimeProcessComponent) {
            throw new DeployException("Deploy " + uri + " failure!");
        }
    }

    @Override
    public void start() {
        this.smartEngine = this.extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        this.assemblyProcessorExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
                AssemblyProcessorExtensionPoint.class);
        this.providerFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
                ProviderFactoryExtensionPoint.class);
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
    }

    @Override
    public void stop() {

    }

    private ProcessDefinition load(ClassLoader classLoader, String uri) throws DeployException {
        //Read xml file
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in = classLoader.getResourceAsStream(uri);
        XMLStreamReader reader;
        try {
            reader = factory.createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            throw new DeployException("Load process config file[" + uri + "] failure!", e);
        }

        //Assembly: process xml file
        ProcessorContext context = new ProcessorContext();
        try {
            boolean findStart = false;
            do {
                int event = reader.next();
                if (event == END_ELEMENT) {
                    break;
                }
                if (event == START_ELEMENT) {
                    findStart = true;
                    break;
                }
            } while (reader.hasNext());
            if (findStart) {
                return (ProcessDefinition) this.assemblyProcessorExtensionPoint.read(reader, context);
            } else {
                throw new DeployException("Read process config file[" + uri + "] failure! Not found start element!");
            }
        } catch (ProcessorReadException | XMLStreamException e) {
            throw new DeployException("Read process config file[" + uri + "] failure!", e);
        }
    }

    private RuntimeProcessComponent install(ClassLoader classLoader, ProcessDefinition definition) {
        //Check
        if (null == definition) {
            return null;
        }

        String processId = definition.getId();
        String version = definition.getVersion();

        if (StringUtils.isBlank(processId) || StringUtils.isBlank(version)) {
            //TODO ettear exception
            return null;
        }

        //Build runtime model;
        DefaultRuntimeProcessComponent processComponent = new DefaultRuntimeProcessComponent();
        processComponent.setId(processId);
        processComponent.setVersion(version);
        processComponent.setClassLoader(classLoader);

        Process process = definition.getProcess();
        DefaultRuntimeProcess runtimeProcess = this.buildRuntimeProcess(process, classLoader);
        if (null != runtimeProcess) {
            ActivityProviderFactory providerFactory = (ActivityProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(
                    process.getClass());
            ActivityProvider activityProvider = providerFactory.createActivityProvider(runtimeProcess);
            runtimeProcess.setProvider(activityProvider);
            processComponent.setProcess(runtimeProcess);
        } else {
            return null;
        }

        this.processContainer.add(processComponent);
        return processComponent;
    }

    private DefaultRuntimeProcess buildRuntimeProcess(Process process, ClassLoader classLoader) {
        if (null == process) {
            return null;
        }
        List<Base> elements = process.getElements();
        if (null == elements || elements.isEmpty()) {
            return null;
        }

        //Build runtime model;
        DefaultRuntimeProcess runtimeProcess = new DefaultRuntimeProcess();
        runtimeProcess.setClassLoader(classLoader);
        runtimeProcess.setModel(process);

        List<DefaultRuntimeSequenceFlow> runtimeSequenceFlows = new ArrayList<>();
        Map<String, RuntimeActivity> runtimeActivities = new HashMap<>();
        for (Base element : elements) {
            if (element instanceof Process) {
                DefaultRuntimeProcess runtimeNode = this.buildRuntimeProcess((Process) element, classLoader);
                runtimeActivities.put(runtimeNode.getId(), runtimeNode);
            } else if (element instanceof SequenceFlow) {
                DefaultRuntimeSequenceFlow runtimeSequenceFlow = new DefaultRuntimeSequenceFlow();

                SequenceFlow sequenceFlow = (SequenceFlow) element;
                runtimeSequenceFlow.setModel(sequenceFlow);

                runtimeSequenceFlows.add(runtimeSequenceFlow);

            } else if (element instanceof Activity) {
                DefaultRuntimeActivity runtimeActivity = new DefaultRuntimeActivity();
                Activity activity = (Activity) element;
                runtimeActivity.setModel(activity);

                runtimeActivities.put(runtimeActivity.getId(), runtimeActivity);
            }
        }

        //Process Sequence Flow
        for (DefaultRuntimeSequenceFlow runtimeSequenceFlow : runtimeSequenceFlows) {
            String sourceRef = runtimeSequenceFlow.getModel().getSourceRef();
            String targetRef = runtimeSequenceFlow.getModel().getTargetRef();
            DefaultRuntimeActivity source = (DefaultRuntimeActivity) runtimeActivities.get(sourceRef);
            DefaultRuntimeActivity target = (DefaultRuntimeActivity) runtimeActivities.get(targetRef);

            runtimeSequenceFlow.setSource(source);
            runtimeSequenceFlow.setTarget(target);
            source.addOutcomeSequenceFlows(runtimeSequenceFlow.getId(), runtimeSequenceFlow);
            target.addIncomeSequenceFlows(runtimeSequenceFlow.getId(), runtimeSequenceFlow);
        }

        //Create Invoker for Sequence Flow
        for (DefaultRuntimeSequenceFlow runtimeSequenceFlow : runtimeSequenceFlows) {
            SequenceFlowProviderFactory providerFactory = (SequenceFlowProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(
                    runtimeSequenceFlow.getModelType());
            SequenceFlowProvider sequenceFlowProvider = providerFactory.createSequenceFlowProvider(runtimeSequenceFlow);
            runtimeSequenceFlow.setProvider(sequenceFlowProvider);
        }

        //Create Invoker for Activity
        for (RuntimeActivity runtimeActivity : runtimeActivities.values()) {
            ActivityProviderFactory providerFactory = (ActivityProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(
                    runtimeActivity.getModelType());
            ActivityProvider activityProvider = providerFactory.createActivityProvider(runtimeActivity);
            ((DefaultRuntimeActivity) runtimeActivity).setProvider(activityProvider);
        }

        runtimeProcess.setActivities(runtimeActivities);

        return runtimeProcess;
    }
}
