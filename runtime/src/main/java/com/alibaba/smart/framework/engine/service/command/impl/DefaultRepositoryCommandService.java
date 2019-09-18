package com.alibaba.smart.framework.engine.service.command.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.DeployException;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extension.scanner.ExtensionBindingResult;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmTransition;
import com.alibaba.smart.framework.engine.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.util.IOUtil;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.model.assembly.Process;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.provider.Invoker;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.provider.factory.ExecutePolicyProviderFactory;
import com.alibaba.smart.framework.engine.provider.factory.InvokerProviderFactory;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmElement;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmActivity;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessDefinition;
//import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmTransition;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.xml.parser.XmlParserExtensionPoint;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;


/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultRepositoryCommandService implements RepositoryCommandService, LifeCycleHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRepositoryCommandService.class);


    private ExtensionPointRegistry extensionPointRegistry;

    private XmlParserExtensionPoint xmlParserExtensionPoint;
    private ProviderFactoryExtensionPoint providerFactoryExtensionPoint;
    private ProcessDefinitionContainer processContainer;

    public DefaultRepositoryCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ProcessDefinition deploy( String classPathUri) throws DeployException {

       ClassLoader classLoader = ClassLoaderUtil.getStandardClassLoader();

        ProcessDefinition definition = this.parse(classLoader, classPathUri);

        putIntoContainer( definition);

        return definition;
    }

    @Override
    public ProcessDefinition deploy(InputStream inputStream) {
        try {
            ProcessDefinition processDefinition = parseStream(inputStream);
            putIntoContainer( processDefinition);

            return processDefinition;
        } catch (Exception e) {
            throw new DeployException("Parse process definition file failure!", e);
        } finally {
            IOUtil.closeQuietly(inputStream);
        }
    }

    @Override
    public ProcessDefinition deployWithUTF8Content(String uTF8ProcessDefinitionContent) {
        byte[] bytes ;
        try {
            bytes = uTF8ProcessDefinitionContent.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(),e);
            throw new EngineException(e);
        }
        InputStream stream = new ByteArrayInputStream(bytes);
        ProcessDefinition processDefinition =  this.deploy(stream);
        return  processDefinition;

    }

    @Override
    public void start() {

        this.xmlParserExtensionPoint = extensionPointRegistry.getExtensionPoint(XmlParserExtensionPoint.class);
        this.providerFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(ProviderFactoryExtensionPoint.class);
        this.processContainer = extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
        //this.defaultExecutePolicyBehavior=extensionPointRegistry.getExtensionPoint(ExecutePolicyBehavior.class);
    }

    @Override
    public void stop() {

    }

    private ProcessDefinition parse(ClassLoader classLoader, String uri) throws DeployException {

        InputStream inputStream = null;
        try {
            inputStream = classLoader.getResourceAsStream(uri);

            if(null == inputStream){
                throw new IllegalArgumentException("Cant find any resources for the uri:"+uri);
            }

            ProcessDefinition processDefinition = parseStream(inputStream);
            return processDefinition;

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
            Object parseResult = this.xmlParserExtensionPoint.parseElement(reader, context);
            return (ProcessDefinition)parseResult;
        } else {
            throw new DeployException("Read process definition file failure! Not found start element!");
        }
    }

    @SuppressWarnings("rawtypes")
    private void putIntoContainer(ProcessDefinition processDefinition) {

        if (null == processDefinition) {
            throw new EngineException("null processDefinition found");
        }

        String processDefinitionId = processDefinition.getId();
        String version = processDefinition.getVersion();



        if (StringUtil.isEmpty(processDefinitionId) || StringUtil.isEmpty(version)) {
            throw new EngineException("empty processDefinitionId or version");
        }


        PvmProcessDefinition pvmProcessDefinition = this.buildPvmProcessDefinition(processDefinition, false);

        this.processContainer.install(pvmProcessDefinition, processDefinition);
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

            //TUNE ocp
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

                    //TUNE support subProcess
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

                    DefaultPvmTransition pvmTransition = new DefaultPvmTransition(this.extensionPointRegistry);
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

                    DefaultPvmActivity pvmActivity = new DefaultPvmActivity(this.extensionPointRegistry);
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

            ExtensionBindingResult extensionBindingResult = SimpleAnnotationScanner.getMap().get(
                ExtensionConstant.ACTIVITY_BEHAVIOR);

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

            for (Map.Entry<String, PvmTransition> runtimeTransitionEntry : pvmTransitionMap.entrySet()) {
                PvmTransition runtimeTransition = runtimeTransitionEntry.getValue();
                this.initElement(runtimeTransition);

                Class aClass = extensionBindingResult.getBindings().get(TransitionBehavior.class.getName());

                //TUNE 强转
                Object o = ClassLoaderUtil.createNewInstance(aClass);

                runtimeTransition.setBehavior((TransitionBehavior)o);

            }

            for (Map.Entry<String, PvmActivity> pvmActivityEntry : pvmActivityMap.entrySet()) {
                PvmActivity pvmActivity = pvmActivityEntry.getValue();

                this.initElement(pvmActivity);

                Activity activity=pvmActivity.getModel();

                String name = activity.getClass().getName();
                Class aClass = extensionBindingResult.getBindings().get(name);

                if(null == aClass){
                    throw new EngineException("Behavior class can't be null for "+name);
                }

                //TUNE initliazer

                Object o = ClassLoaderUtil.createNewInstance(aClass);

                AbstractActivityBehavior o1 = (AbstractActivityBehavior)o;
                o1.setPvmActivity(pvmActivity);
                o1.setExtensionPointRegistry(extensionPointRegistry);

                o1.setProcessInstanceFactory(extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class));
                o1.setExecutionInstanceFactory(extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class));
                o1.setActivityInstanceFactory(extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class));
                o1.setTaskInstanceFactory(extensionPointRegistry.getExtensionPoint(TaskInstanceFactory.class));
                o1.setProcessEngineConfiguration( extensionPointRegistry.getExtensionPoint(
                    SmartEngine.class).getProcessEngineConfiguration());
                PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
                o1.setExecutionInstanceStorage(persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class));


                pvmActivity.setBehavior(o1);


            }

            pvmProcessDefinition.setActivities(pvmActivityMap);
            pvmProcessDefinition.setTransitions(pvmTransitionMap);
        }
        return pvmProcessDefinition;
    }

    /**
     * Init Element
     *
     * @param pvmElement element
     */
    private void initElement(PvmElement pvmElement) {
        //Init extensions
        Extensions extensions = pvmElement.getModel().getExtensions();
        if (null != extensions) {
            List<Extension> extensionList = extensions.getExtensions();
            if (null != extensionList && !extensionList.isEmpty()) {
                List<Invoker> prepareExtensionInvokers = new ArrayList<Invoker>();
                List<Invoker> extensionInvokers = new ArrayList<Invoker>();
                for (Extension extension : extensionList) {
                    InvokerProviderFactory providerFactory = (InvokerProviderFactory)this.providerFactoryExtensionPoint
                        .getProviderFactory(extension.getClass());
                    if (null == providerFactory) {
                        LOGGER.debug("No factory found for " + extension.getClass());
                        continue;
                    }
                    Invoker extensionInvoker = providerFactory.createInvoker(extension);
                        prepareExtensionInvokers.add(extensionInvoker);
                        extensionInvokers.add(extensionInvoker);

                }
                //pvmElement.setPrepareExtensionInvokers(prepareExtensionInvokers);
                //pvmElement.setExtensionInvokers(extensionInvokers);
            }
        }


    }
}
