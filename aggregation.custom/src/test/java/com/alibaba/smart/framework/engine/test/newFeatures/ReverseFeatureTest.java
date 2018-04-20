package com.alibaba.smart.framework.engine.test.newFeatures;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.test.AliPayIdGenerator;

import org.junit.Test;

/**
 * Created by niefeng on 2018/4/19.
 */
public class ReverseFeatureTest {
    @Test
    public void test(){
        PersisterSession.create();
        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new AliPayIdGenerator());
        //processEngineConfiguration.setPersisterStrategy(new AliPayPersisterStrategy());

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("reverse-features.bpmn20.xml");


        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction","pre_order");

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),request
        );
    }
}
