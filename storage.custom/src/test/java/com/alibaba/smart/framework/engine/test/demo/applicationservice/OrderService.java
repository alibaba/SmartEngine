package com.alibaba.smart.framework.engine.test.demo.applicationservice;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.persister.PersisterStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializer;
import com.alibaba.smart.framework.engine.persister.util.WorkFlowSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;
import com.alibaba.smart.framework.engine.test.AliPayPersisterStrategy;
import com.alibaba.smart.framework.engine.test.demo.dto.OrderDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  22:36.
 */
public class OrderService {

    private static ProcessCommandService processCommandService = null;
    private static ExecutionInstanceQueryService executionQueryService = null;
    private static ExecutionCommandService executionCommandService = null;
    private static RepositoryCommandService repositoryCommandService = null;
    private static ProcessDefinition processDefinition = null;
    private static PersisterStrategy persisterStrategy = new AliPayPersisterStrategy();


    static {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
          processCommandService = smartEngine.getProcessService();
          executionQueryService = smartEngine.getExecutionQueryService();
          executionCommandService = smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        repositoryCommandService = smartEngine
                .getRepositoryService();
        processDefinition =   repositoryCommandService
                .deploy("alipay-forex.bpmn20.xml");
    }

    public OrderDTO createOrder(String blalbla){

        OrderDTO orderDTO =new OrderDTO();
        Long orderId = 123L; //TODO 确定下生成策略。


        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction","pre_order");

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),request
        );

        WorkFlowSession.currentSession().setProcessInstance(processInstance);

        List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        //完成预下单,将流程驱动到 下单确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        String string =  InstanceSerializer.serialize(processInstance);

        persisterStrategy.update(orderId,string);

        orderDTO.setBlabla(blalbla);

        return orderDTO;
    }

    private void persisteAndUpdateThreadLocal(long orderId, ProcessInstance processInstance) {

        // 存储到业务系统里面
        String string =  InstanceSerializer.serialize(processInstance);
        persisterStrategy.update(orderId,string);

        // 注意:在执行之前,更新下ThreadLocal。另外,在线上环境,使用完毕后需要clean 下 ThreadLocal。
        processInstance =  InstanceSerializer.deserializeAll(string);

        WorkFlowSession.currentSession().setProcessInstance(processInstance);
    }

}
