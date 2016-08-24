package com.alibaba.smart.framework.engine.service.impl;

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
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.DeployException;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.IOUtil;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Process;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ProviderRegister;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessComponent;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmActivity;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmTransition;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultRuntimeProcessComponent;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.util.ThreadLocalUtil;
import com.alibaba.smart.framework.engine.xml.parser.AssemblyParserExtensionPoint;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * 默认部署器 Created by ettear on 16-4-13.
 * 
 *TODO deploy 完成后,需要被获取流程定义 .
 */
public class DefaultRepositoryService implements RepositoryService, LifeCycleListener {

    /**
     * 扩展点注册器
     */
    private SmartEngine                   smartEngine;
    private AssemblyParserExtensionPoint  assemblyParserExtensionPoint;
    private ProviderFactoryExtensionPoint providerFactoryExtensionPoint;
    private ProcessDefinitionContainer              processContainer;

 

    @Override
    public ProcessDefinition deploy(String uri) throws DeployException {
        return this.deploy(null, uri);
    }

    @Override
    public ProcessDefinition deploy(String moduleName, String uri) throws DeployException {
        
        ClassLoader classLoader = this.smartEngine.getClassLoader(moduleName); 
        
        if (null == classLoader) {
            throw new DeployException("Module[" + moduleName + "] not found!");
        }

        //TODO 支持从不同地方加载 
        ProcessDefinition definition = this.parse(classLoader, uri);
        PvmProcessComponent runtimeProcessComponent = install(classLoader, definition);
        if (null == runtimeProcessComponent) {
            throw new DeployException("Deploy " + uri + " failure!");
        }
        return definition;
    }

    @Override
    public void start() {
        
        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();
        this.smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        this.assemblyParserExtensionPoint = extensionPointRegistry.getExtensionPoint(AssemblyParserExtensionPoint.class);
        this.providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(ProviderFactoryExtensionPoint.class);
        this.processContainer = extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
    }

    @Override
    public void stop() {

    }

    private ProcessDefinition parse(ClassLoader classLoader, String uri) throws DeployException {

        InputStream in = null;
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            in = classLoader.getResourceAsStream(uri);

            XMLStreamReader reader = factory.createXMLStreamReader(in);

            ParseContext context = new ParseContext();

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
        } finally {
            IOUtil.closeQuietly(in);
        }
    }

    @SuppressWarnings("rawtypes")
    private PvmProcessComponent install(ClassLoader classLoader, ProcessDefinition definition) {

        if (null == definition) {
            throw new EngineException("null definition found");
        }

        String processId = definition.getId();
        String version = definition.getVersion();

        if (StringUtils.isBlank(processId) || StringUtils.isBlank(version)) {
            throw new EngineException("empty processId or version");
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
            //TODO 这里应该是FALSE吧?
            PvmProcessDefinition runtimeProcess = this.buildPvmProcessDefinition(process, processComponent, true);
            if (null != runtimeProcess && runtimeProcess instanceof ProviderRegister) {
                ActivityProviderFactory providerFactory = (ActivityProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(process.getClass());
                ActivityProvider activityProvider = providerFactory.createActivityProvider(runtimeProcess);
                ((ProviderRegister) runtimeProcess).registerProvider(activityProvider);
                processComponent.setProcess(runtimeProcess);
            } else {
                return null;
            }
        }

        this.processContainer.install(processComponent);
        return processComponent;
    }

    @SuppressWarnings("rawtypes")
    private PvmProcessDefinition buildPvmProcessDefinition(Process process, DefaultRuntimeProcessComponent component, boolean sub) {
        String idPrefix = "";
        if (sub) {
            idPrefix = process.getId() + "_";
        }

        int index = 0;

        // Build runtime model;
        DefaultPvmProcessDefinition pvmProcessDefinition = new DefaultPvmProcessDefinition();
        pvmProcessDefinition.setClassLoader(component.getClassLoader());
        pvmProcessDefinition.setModel(process);
        component.addProcess(pvmProcessDefinition.getModel().getId(), pvmProcessDefinition);

        List<BaseElement> elements = process.getElements();
        if (null != elements && !elements.isEmpty()) {

            //TODO ocp 
            Map<String, PvmTransition> runtimeTransitions = new HashMap<>();
            Map<String, PvmActivity> runtimeActivities = new HashMap<>();
            for (BaseElement element : elements) {
                if (element instanceof Process) {
                    Process subProcess = (Process) element;

                    if (StringUtils.isBlank(subProcess.getId())) {
                        subProcess.setId(idPrefix + "process" + index);
                    }
                    index++;

                    PvmProcessDefinition runtimeSubProcess = this.buildPvmProcessDefinition(subProcess, component, true);
                    runtimeActivities.put(runtimeSubProcess.getModel().getId(), runtimeSubProcess);

                    if (runtimeSubProcess.getModel().isStartActivity()) {
                        pvmProcessDefinition.setStartActivity(runtimeSubProcess);
                    }
                } else if (element instanceof Transition) {
                    Transition transition = (Transition) element;

                    if (StringUtils.isBlank(transition.getId())) {
                        transition.setId(idPrefix + "transition" + index);
                    }
                    index++;

                    DefaultPvmTransition runtimeTransition = new DefaultPvmTransition();
                    runtimeTransition.setModel(transition);

                    runtimeTransitions.put(runtimeTransition.getModel().getId(), runtimeTransition);

                } else if (element instanceof Activity) {
                    Activity activity = (Activity) element;

                    if (StringUtils.isBlank(activity.getId())) {
                        activity.setId(idPrefix + "activity" + index);
                    }
                    index++;

                    DefaultPvmActivity runtimeActivity = new DefaultPvmActivity();
                    runtimeActivity.setModel(activity);

                    runtimeActivities.put(runtimeActivity.getModel().getId(), runtimeActivity);

                    if (runtimeActivity.getModel().isStartActivity()) {
                        pvmProcessDefinition.setStartActivity(runtimeActivity);
                    }
                }
            }

            // Process Transition Flow
            for (Map.Entry<String, PvmTransition> runtimeTransitionEntry : runtimeTransitions.entrySet()) {
                DefaultPvmTransition runtimeTransition = (DefaultPvmTransition) runtimeTransitionEntry.getValue();
                String sourceRef = runtimeTransition.getModel().getSourceRef();
                String targetRef = runtimeTransition.getModel().getTargetRef();
                DefaultPvmActivity source = (DefaultPvmActivity) runtimeActivities.get(sourceRef);
                DefaultPvmActivity target = (DefaultPvmActivity) runtimeActivities.get(targetRef);

                runtimeTransition.setSource(source);
                runtimeTransition.setTarget(target);
                source.addOutcomeTransition(runtimeTransition.getModel().getId(), runtimeTransition);
                target.addIncomeTransition(runtimeTransition.getModel().getId(), runtimeTransition);
            }

            // Create Invoker for Transition Flow
            for (Map.Entry<String, PvmTransition> runtimeTransitionEntry : runtimeTransitions.entrySet()) {
                PvmTransition runtimeTransition = runtimeTransitionEntry.getValue();
                TransitionProviderFactory providerFactory = (TransitionProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(runtimeTransition.getModel().getClass());

                if (null == providerFactory) {
                    throw new RuntimeException("No factory found for " + runtimeTransition.getModel().getClass());
                }

                TransitionProvider transitionProvider = providerFactory.createTransitionProvider(runtimeTransition);
                runtimeTransition.registerProvider(transitionProvider);
            }

            // Create Invoker for Activity
            for (Map.Entry<String, PvmActivity> runtimeActivityEntry : runtimeActivities.entrySet()) {
                PvmActivity runtimeActivity = runtimeActivityEntry.getValue();
                ActivityProviderFactory providerFactory = (ActivityProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(runtimeActivity.getModel().getClass());

                if (null == providerFactory) {
                    throw new RuntimeException("No factory found for " + runtimeActivity.getModel().getClass());
                }

                ActivityProvider activityProvider = providerFactory.createActivityProvider(runtimeActivity);
                runtimeActivity.registerProvider(activityProvider);
            }

            pvmProcessDefinition.setActivities(runtimeActivities);
            pvmProcessDefinition.setTransitions(runtimeTransitions);
        }
        return pvmProcessDefinition;
    }
}
