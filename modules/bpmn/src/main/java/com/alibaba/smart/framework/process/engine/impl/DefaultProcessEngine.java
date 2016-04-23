package com.alibaba.smart.framework.process.engine.impl;

import java.util.List;

import lombok.Setter;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.EngineException;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.deployment.exception.DeployException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.process.context.ProcessContext;
import com.alibaba.smart.framework.process.context.ProcessContextHolder;
import com.alibaba.smart.framework.process.engine.ProcessEngine;
import com.alibaba.smart.framework.process.service.RepositoryService;
import com.alibaba.smart.framework.process.service.RuntimeService;
import com.alibaba.smart.framework.process.service.TaskService;
import com.alibaba.smart.framework.process.service.impl.DefaultRuntimeService;
import com.alibaba.smart.framework.process.service.impl.DefaultTaskService;


public class DefaultProcessEngine implements ProcessEngine{
    //TODO 剥离接口,API,支持远程调用,结合初期目标
    @Setter
    private RepositoryService repositoryService;
    @Setter
    private RuntimeService runtimeService;
    @Setter
    private TaskService taskService;
    
    private ProcessContext processContext;
    
    
    
    private DefaultSmartEngine smartEngine;
    
     

    @Override
    public void init(ProcessEngineConfiguration processEngineConfiguration) {
        //TODO SHOULD BE INIT ONLY ONCE,ADD CHECK, netty ,dubbo 
        try {
            smartEngine = new DefaultSmartEngine();
            smartEngine.start();

            Deployer   deployer = smartEngine.getDeployer();
            ExtensionPointRegistry extensionPointRegistry = smartEngine.getExtensionPointRegistry();
            ProcessContainer processContainer = extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
            
            
            List<String> processDefinitions =   processEngineConfiguration.getProcessDefinitions();
            
            
            for (String processDefinitionResource : processDefinitions) {
                deployer.deploy(processDefinitionResource);
            }
            
            
            
            //TODO 支持注入,参考activiti
            this.runtimeService = new DefaultRuntimeService();
            this.taskService = new DefaultTaskService();
            this.processContext = new ProcessContext();
            this.processContext.setProcessContainer(processContainer);
            ProcessContextHolder.set(processContext);
            
        } catch (EngineException e) {
            //FIXME 是否需要显式抛出异常?
            e.printStackTrace();
        } catch (DeployException e) {
            //FIXME
            e.printStackTrace();
        }
      
        
        
    }

    @Override
    public RuntimeService getRuntimeService() {
      return this.runtimeService;
    }

    @Override
    public TaskService getTaskService() {
        return this.taskService;
    }

//    @Override
//    public RepositoryService getRepositoryService() {
//        // TODO Auto-generated method stub
//        return null;
//    }

}
