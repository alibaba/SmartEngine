//package com.alibaba.smart.framework.process.engine.impl;
//
//import java.util.List;
//
//import lombok.Setter;
//
//import com.alibaba.smart.framework.engine.DefaultSmartEngine;
//import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
//import com.alibaba.smart.framework.engine.exception.DeployException;
//import com.alibaba.smart.framework.engine.exception.EngineException;
//import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
//import com.alibaba.smart.framework.process.configuration.ProcessEngineConfiguration;
//import com.alibaba.smart.framework.process.context.ProcessContext;
//import com.alibaba.smart.framework.process.context.ProcessContextHolder;
//import com.alibaba.smart.framework.process.engine.ProcessEngine;
//import com.alibaba.smart.framework.process.service.RepositoryCommandService;
//import com.alibaba.smart.framework.process.service.RuntimeService;
//import com.alibaba.smart.framework.process.service.TaskCommandService;
//import com.alibaba.smart.framework.process.service.impl.DefaultRuntimeService;
//import com.alibaba.smart.framework.process.service.impl.DefaultTaskCommandService;
//
//public class DefaultProcessEngine implements ProcessEngine {
//
//    // TODO 剥离接口,API,支持远程调用,结合初期目标
//    @Setter
//    private RepositoryCommandService      repositoryService;
//    @Setter
//    private RuntimeService         runtimeService;
//    @Setter
//    private TaskCommandService            taskService;
//
//    private ProcessContext         processContext;
//
//    private ExtensionPointRegistry extensionPointRegistry;
//
//    private DefaultSmartEngine     smartEngine;
//
//    @Override
//    public void init(ProcessEngineConfiguration processEngineConfiguration) {
//        // TODO SHOULD BE INIT ONLY ONCE,ADD CHECK, netty ,dubbo
//        try {
//            this.smartEngine = new DefaultSmartEngine();
//            this.smartEngine.start();
//
//            RepositoryCommandService deployer = smartEngine.getRepositoryService();
//            this.extensionPointRegistry = smartEngine.getExtensionPointRegistry();
//            ProcessContainer processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
//
//            List<String> processDefinitions = processEngineConfiguration.getProcessDefinitions();
//
//            for (String processDefinitionResource : processDefinitions) {
//                deployer.deploy(processDefinitionResource);
//            }
//
//            // TODO 支持注入,参考activiti
//            this.runtimeService = new DefaultRuntimeService();
//            this.taskService = new DefaultTaskCommandService();
//
//            this.processContext = new ProcessContext();
//            this.processContext.setProcessContainer(processContainer);
//            this.processContext.setProcessEngine(this);
//            ProcessContextHolder.set(processContext);
//
//        } catch (EngineException e) {
//            // FIXME 是否需要显式抛出异常?
//            e.printStackTrace();
//        } catch (DeployException e) {
//            // FIXME
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public RuntimeService getRuntimeService() {
//        return this.runtimeService;
//    }
//
//    @Override
//    public TaskCommandService getTaskCommandService() {
//        return this.taskService;
//    }
//
//    @Override
//    public ExtensionPointRegistry getExtensionPointRegistry() {
//        return this.extensionPointRegistry;
//
//    }
//
//    // @Override
//    // public RepositoryCommandService getRepositoryService() {
//    // // TODO Auto-generated method stub
//    // return null;
//    // }
//
//}
