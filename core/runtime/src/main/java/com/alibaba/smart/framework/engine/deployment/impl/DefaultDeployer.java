package com.alibaba.smart.framework.engine.deployment.impl;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.assembly.Transition;
import com.alibaba.smart.framework.engine.assembly.parser.AssemblyParserExtensionPoint;
import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.deployment.exception.DeployException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.runtime.ProviderRuntimeInvocable;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeProcessComponent;
import com.alibaba.smart.framework.engine.runtime.impl.DefaultRuntimeTransition;

/**
 * 默认部署器 Created by ettear on 16-4-13.
 */
public class DefaultDeployer implements Deployer, LifeCycleListener {

    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry        extensionPointRegistry;
    private SmartEngine                   smartEngine;
    private AssemblyParserExtensionPoint  assemblyParserExtensionPoint;
    private ProviderFactoryExtensionPoint providerFactoryExtensionPoint;
    private ProcessContainer              processContainer;

    public DefaultDeployer(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void deploy(String uri) throws DeployException {
        this.deploy(null, uri);
    }

    @Override
    public void deploy(String moduleName, String uri) throws DeployException {
        ClassLoader classLoader = this.smartEngine.getClassLoader(moduleName);// Find class loader
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
        this.assemblyParserExtensionPoint = this.extensionPointRegistry.getExtensionPoint(AssemblyParserExtensionPoint.class);
        this.providerFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(ProviderFactoryExtensionPoint.class);
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
    }

    @Override
    public void stop() {

    }

    private ProcessDefinition load(ClassLoader classLoader, String uri) throws DeployException {
        // Read xml file
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in = classLoader.getResourceAsStream(uri);

        // FIXME close stream, diqi
        XMLStreamReader reader;
        try {
            reader = factory.createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            throw new DeployException("Load process config file[" + uri + "] failure!", e);
        }

        // Assembly: process xml file
        ParseContext context = new ParseContext();
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
                return (ProcessDefinition) this.assemblyParserExtensionPoint.parse(reader, context);
            } else {
                throw new DeployException("Read process config file[" + uri + "] failure! Not found start element!");
            }
        } catch (ParseException | XMLStreamException e) {
            throw new DeployException("Read process config file[" + uri + "] failure!", e);
        }
    }

    private RuntimeProcessComponent install(ClassLoader classLoader, ProcessDefinition definition) {
        // Check
        if (null == definition) {
            return null;
        }

        String processId = definition.getId();
        String version = definition.getVersion();

        if (StringUtils.isBlank(processId) || StringUtils.isBlank(version)) {
            // TODO ettear exception
            return null;
        }

        // Build runtime model;
        DefaultRuntimeProcessComponent processComponent = new DefaultRuntimeProcessComponent();
        processComponent.setId(processId);
        processComponent.setVersion(version);
        processComponent.setClassLoader(classLoader);

        Process process = definition.getProcess();
        if (null != process) {
            if (StringUtils.isBlank(process.getId())) {
                process.setId("default");
            }
            RuntimeProcess runtimeProcess = this.buildRuntimeProcess(process, processComponent, true);
            if (null != runtimeProcess && runtimeProcess instanceof ProviderRuntimeInvocable) {
                ActivityProviderFactory providerFactory = (ActivityProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(process.getClass());
                ActivityProvider activityProvider = providerFactory.createActivityProvider(runtimeProcess);
                ((ProviderRuntimeInvocable) runtimeProcess).setProvider(activityProvider);
                processComponent.setProcess(runtimeProcess);
            } else {
                return null;
            }
        }

        this.processContainer.install(processComponent);
        return processComponent;
    }

    private RuntimeProcess buildRuntimeProcess(Process process, DefaultRuntimeProcessComponent component, boolean sub) {
        String idPrefix = "";
        if (sub) {
            idPrefix = process.getId() + "_";
        }

        int index = 0;

        // Build runtime model;
        DefaultRuntimeProcess runtimeProcess = new DefaultRuntimeProcess();
        runtimeProcess.setExtensionPointRegistry(this.extensionPointRegistry);
        runtimeProcess.setClassLoader(component.getClassLoader());
        runtimeProcess.setModel(process);
        component.addProcess(runtimeProcess.getId(), runtimeProcess);

        List<Base> elements = process.getElements();
        if (null != elements && !elements.isEmpty()) {

            Map<String, RuntimeTransition> runtimeTransitions = new HashMap<>();
            Map<String, RuntimeActivity> runtimeActivities = new HashMap<>();
            for (Base element : elements) {
                if (element instanceof Process) {
                    Process subProcess = (Process) element;

                    if (StringUtils.isBlank(subProcess.getId())) {
                        subProcess.setId(idPrefix + "process" + index);
                    }
                    index++;

                    RuntimeProcess runtimeSubProcess = this.buildRuntimeProcess(subProcess, component, true);
                    runtimeActivities.put(runtimeSubProcess.getId(), runtimeSubProcess);

                    if (runtimeSubProcess.isStartActivity()) {
                        runtimeProcess.setStartActivity(runtimeSubProcess);
                    }
                } else if (element instanceof Transition) {
                    Transition transition = (Transition) element;

                    if (StringUtils.isBlank(transition.getId())) {
                        transition.setId(idPrefix + "transition" + index);
                    }
                    index++;

                    DefaultRuntimeTransition runtimeTransition = new DefaultRuntimeTransition();
                    runtimeTransition.setExtensionPointRegistry(this.extensionPointRegistry);
                    runtimeTransition.setModel(transition);

                    runtimeTransitions.put(runtimeTransition.getId(), runtimeTransition);

                } else if (element instanceof Activity) {
                    Activity activity = (Activity) element;

                    if (StringUtils.isBlank(activity.getId())) {
                        activity.setId(idPrefix + "activity" + index);
                    }
                    index++;

                    DefaultRuntimeActivity runtimeActivity = new DefaultRuntimeActivity();
                    runtimeActivity.setExtensionPointRegistry(this.extensionPointRegistry);
                    runtimeActivity.setModel(activity);

                    runtimeActivities.put(runtimeActivity.getId(), runtimeActivity);

                    if (runtimeActivity.isStartActivity()) {
                        runtimeProcess.setStartActivity(runtimeActivity);
                    }
                }
            }

            // Process Transition Flow
            for (Map.Entry<String, RuntimeTransition> runtimeTransitionEntry : runtimeTransitions.entrySet()) {
                DefaultRuntimeTransition runtimeTransition = (DefaultRuntimeTransition) runtimeTransitionEntry.getValue();
                String sourceRef = runtimeTransition.getModel().getSourceRef();
                String targetRef = runtimeTransition.getModel().getTargetRef();
                DefaultRuntimeActivity source = (DefaultRuntimeActivity) runtimeActivities.get(sourceRef);
                DefaultRuntimeActivity target = (DefaultRuntimeActivity) runtimeActivities.get(targetRef);

                runtimeTransition.setSource(source);
                runtimeTransition.setTarget(target);
                source.addOutcomeTransition(runtimeTransition.getId(), runtimeTransition);
                target.addIncomeTransition(runtimeTransition.getId(), runtimeTransition);
            }

            // Create Invoker for Transition Flow
            for (Map.Entry<String, RuntimeTransition> runtimeTransitionEntry : runtimeTransitions.entrySet()) {
                RuntimeTransition runtimeTransition = runtimeTransitionEntry.getValue();
                if (runtimeTransition instanceof ProviderRuntimeInvocable) {
                    TransitionProviderFactory providerFactory = (TransitionProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(runtimeTransition.getModelType());

                    if (null == providerFactory) {
                        // TODO XX
                        throw new RuntimeException("No factory found for " + runtimeTransition.getModelType());
                    }

                    TransitionProvider transitionProvider = providerFactory.createTransitionProvider(runtimeTransition);
                    ((ProviderRuntimeInvocable) runtimeTransition).setProvider(transitionProvider);
                }
            }

            // Create Invoker for Activity
            for (Map.Entry<String, RuntimeActivity> runtimeActivityEntry : runtimeActivities.entrySet()) {
                RuntimeActivity runtimeActivity = runtimeActivityEntry.getValue();
                if (runtimeActivity instanceof ProviderRuntimeInvocable) {
                    ActivityProviderFactory providerFactory = (ActivityProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(runtimeActivity.getModelType());

                    if (null == providerFactory) {
                        // TODO XX
                        throw new RuntimeException("No factory found for " + runtimeActivity.getModelType());
                    }

                    ActivityProvider activityProvider = providerFactory.createActivityProvider(runtimeActivity);
                    ((ProviderRuntimeInvocable) runtimeActivity).setProvider(activityProvider);
                }
            }

            runtimeProcess.setActivities(runtimeActivities);
            runtimeProcess.setTransitions(runtimeTransitions);
        }
        return runtimeProcess;
    }
}
