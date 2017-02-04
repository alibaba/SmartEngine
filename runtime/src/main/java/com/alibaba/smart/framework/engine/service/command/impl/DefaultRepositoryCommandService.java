package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.DeployException;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.instance.util.IOUtil;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.*;
import com.alibaba.smart.framework.engine.model.assembly.Process;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmActivity;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmTransition;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.xml.parser.AssemblyParserExtensionPoint;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;


/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultRepositoryCommandService implements RepositoryCommandService, LifeCycleListener {

    /**
     * 扩展点注册器
     */
    private SmartEngine smartEngine;
    private ExtensionPointRegistry extensionPointRegistry;

    private AssemblyParserExtensionPoint assemblyParserExtensionPoint;
    private ProviderFactoryExtensionPoint providerFactoryExtensionPoint;
    private ProcessDefinitionContainer processContainer;

    public DefaultRepositoryCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ProcessDefinition deploy( String classPathUri) throws DeployException {

       ClassLoader classLoader = ClassLoaderUtil.getStandardClassLoader();

        ProcessDefinition definition = this.parse(classLoader, classPathUri);
        install( definition);

        return definition;
    }

    @Override
    public ProcessDefinition deploy(InputStream inputStream) {
        try {
            return parseStream(inputStream);
        } catch (Exception e) {
            throw new DeployException("Parse process definition file failure!", e);
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
    }

    @Override
    public void start() {

        this.smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        this.assemblyParserExtensionPoint = extensionPointRegistry.getExtensionPoint(AssemblyParserExtensionPoint.class);
        this.providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(ProviderFactoryExtensionPoint.class);
        this.processContainer = extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
    }

    @Override
    public void stop() {

    }

    private ProcessDefinition parse(ClassLoader classLoader, String uri) throws DeployException {

        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream(uri);

            if(null == inputStream){
                throw new IllegalArgumentException("Cant findAll any resources for the uri:"+uri);
            }

            return parseStream(inputStream);
        } catch (Exception e) {
            throw new DeployException("Read process definition file[" + uri + "] failure!", e);
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
    }

    private ProcessDefinition parseStream(InputStream inputStream) throws XMLStreamException, ParseException {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

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
            throw new DeployException("Read process definition file failure! Not found start element!");
        }
    }

    @SuppressWarnings("rawtypes")
    private ProcessDefinitionContainer install(ProcessDefinition processDefinition) {

        if (null == processDefinition) {
            throw new EngineException("null processDefinition found");
        }

        String processId = processDefinition.getId();
        String version = processDefinition.getVersion();

        if (StringUtil.isEmpty(processId) || StringUtil.isEmpty(version)) {
            throw new EngineException("empty processId or version");
        }


        PvmProcessDefinition pvmProcessDefinition = this.buildPvmProcessDefinition(processDefinition, false);

        this.processContainer.install(pvmProcessDefinition);
        return processContainer;
    }

    @SuppressWarnings("rawtypes")
    private PvmProcessDefinition buildPvmProcessDefinition(ProcessDefinition processDefinition,  boolean sub) {


        Process process = processDefinition.getProcess();
        String idPrefix = "";
        if (sub) {
            idPrefix = process.getId() + "_";
        }

        int index = 0;

        DefaultPvmProcessDefinition pvmProcessDefinition = new DefaultPvmProcessDefinition();
        pvmProcessDefinition.setId(processDefinition.getId());
        pvmProcessDefinition.setVersion(processDefinition.getVersion());


        pvmProcessDefinition.setModel(process);

        List<BaseElement> elements = process.getElements();
        if (null != elements && !elements.isEmpty()) {

            //TODO ocp 
            Map<String, PvmTransition> pvmTransitionMap = new HashMap<String, PvmTransition>();
            Map<String, PvmActivity> pvmActivityMap = new HashMap<String, PvmActivity>();
            for (BaseElement element : elements) {
                if (element instanceof Process) {
                    Process subProcess = (Process) element;

                    if (StringUtil.isEmpty(subProcess.getId())) {
                        subProcess.setId(idPrefix + "process" + index);
                    }
                    index++;

//                    PvmProcessDefinition processDefinition = this.buildPvmProcessDefinition(subProcess, true);

                    //TODO support subProcess
//                    pvmActivityMap.put(processDefinition.getModel().getId(), processDefinition);
//
//                    if (processDefinition.getModel().isStartActivity()) {
//                        pvmProcessDefinition.setStartActivity(processDefinition);
//                    }
                } else if (element instanceof Transition) {
                    Transition transition = (Transition) element;

                    if (StringUtil.isEmpty(transition.getId())) {
                        transition.setId(idPrefix + "transition" + index);
                    }
                    index++;

                    DefaultPvmTransition pvmTransition = new DefaultPvmTransition();
                    pvmTransition.setModel(transition);

                    String id = pvmTransition.getModel().getId();


                    PvmTransition oldValue =    pvmTransitionMap.put(id, pvmTransition);
                    if(null != oldValue){
                        throw new EngineException("duplicated id found for "+id);
                    }

                } else if (element instanceof Activity) {
                    Activity activity = (Activity) element;

                    if (StringUtil.isEmpty(activity.getId())) {
                        activity.setId(idPrefix + "activity" + index);
                    }
                    index++;

                    DefaultPvmActivity pvmActivity = new DefaultPvmActivity();
                    pvmActivity.setModel(activity);

                    String id = pvmActivity.getModel().getId();


                    PvmActivity oldValue =   pvmActivityMap.put(id, pvmActivity);
                    if(null != oldValue){
                        throw new EngineException("duplicated id found for "+id);
                    }


                    if (pvmActivity.getModel().isStartActivity()) {
                        pvmProcessDefinition.setStartActivity(pvmActivity);
                    }
                }
            }

            // Process Transition Flow
            for (Map.Entry<String, PvmTransition> runtimeTransitionEntry : pvmTransitionMap.entrySet()) {
                DefaultPvmTransition runtimeTransition = (DefaultPvmTransition) runtimeTransitionEntry.getValue();
                String sourceRef = runtimeTransition.getModel().getSourceRef();
                String targetRef = runtimeTransition.getModel().getTargetRef();
                DefaultPvmActivity source = (DefaultPvmActivity) pvmActivityMap.get(sourceRef);
                DefaultPvmActivity target = (DefaultPvmActivity) pvmActivityMap.get(targetRef);

                runtimeTransition.setSource(source);
                runtimeTransition.setTarget(target);
                source.addOutcomeTransition(runtimeTransition.getModel().getId(), runtimeTransition);
                target.addIncomeTransition(runtimeTransition.getModel().getId(), runtimeTransition);
            }

            // Create Invoker for Transition Flow
            for (Map.Entry<String, PvmTransition> runtimeTransitionEntry : pvmTransitionMap.entrySet()) {
                PvmTransition runtimeTransition = runtimeTransitionEntry.getValue();
                TransitionProviderFactory providerFactory = (TransitionProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(runtimeTransition.getModel().getClass());

                if (null == providerFactory) {
                    throw new RuntimeException("No factory found for " + runtimeTransition.getModel().getClass());
                }

                TransitionBehavior transitionBehavior = providerFactory.createTransitionProvider(runtimeTransition);
                runtimeTransition.setTransitionBehavior(transitionBehavior);
            }

            // Create Invoker for Activity
            for (Map.Entry<String, PvmActivity> runtimeActivityEntry : pvmActivityMap.entrySet()) {
                PvmActivity runtimeActivity = runtimeActivityEntry.getValue();
                ActivityProviderFactory providerFactory = (ActivityProviderFactory) this.providerFactoryExtensionPoint.getProviderFactory(runtimeActivity.getModel().getClass());

                if (null == providerFactory) {
                    throw new RuntimeException("No factory found for " + runtimeActivity.getModel().getClass());
                }

                ActivityBehavior activityBehavior = providerFactory.createActivityProvider(runtimeActivity);
                runtimeActivity.setActivityBehavior(activityBehavior);
            }

            pvmProcessDefinition.setActivities(pvmActivityMap);
            pvmProcessDefinition.setTransitions(pvmTransitionMap);
        }
        return pvmProcessDefinition;
    }
}
