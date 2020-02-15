package com.alibaba.smart.framework.engine.test.process.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.process.VariableInstanceAndMultiInstanceTest;

import org.junit.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("transactionHelper")
@Transactional
public class TransactionHelper {


    public   void ya(TaskInstanceDAO taskInstanceDAO) {
        TaskInstanceEntity entity = new TaskInstanceEntity();
        entity.setId(System.currentTimeMillis());

        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
        entity.setActivityInstanceId(11L);
        entity.setClaimUserId("assign_id");
        Date claimTime = new Date();
        entity.setClaimTime(new Date(claimTime.getTime()));
        entity.setCompleteTime(new Date(claimTime.getTime() + 1000000));
        entity.setExecutionInstanceId(22L);
        entity.setPriority(333);
        entity.setStatus(TaskInstanceConstant.PENDING);
        entity.setProcessDefinitionActivityId("userTask");
        entity.setProcessInstanceId(444L);
        entity.setComment("comment");
        entity.setExtension("extension");
        entity.setTitle("title");

        taskInstanceDAO.insert(entity);

        throw new RuntimeException("ya");
    }




    @Transactional
    public TaskInstance signal(TaskCommandService taskCommandService, TaskQueryService taskQueryService,
                               ProcessInstance processInstance) {
        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(1,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"1");
        submitFormRequest.put("action", "agree");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, VariableInstanceAndMultiInstanceTest.DISAGREE);

        //6.流程流转:处理 submitTask,完成任务申请.
        taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);
        return submitTaskInstance;
    }

    @Transactional
    public ProcessInstance start(ProcessCommandService processCommandService, ProcessDefinition processDefinition) {
        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion()
        );
        Assert.assertNotNull(processInstance);
        return processInstance;
    }
}