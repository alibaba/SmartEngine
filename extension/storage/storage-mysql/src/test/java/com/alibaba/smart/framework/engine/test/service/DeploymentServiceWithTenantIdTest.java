package com.alibaba.smart.framework.engine.test.service;

import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.LogicStatusConstant;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.param.command.UpdateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.DefaultTaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.util.IOUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DeploymentServiceWithTenantIdTest extends DatabaseBaseTestCase {

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }


    @Test
    public void testSimple() throws Exception {

        String tenantId = "test";
        //2.获得常用服务
        DeploymentCommandService deploymentCommandService = smartEngine.getDeploymentCommandService();
        DeploymentQueryService deploymentQueryService =  smartEngine.getDeploymentQueryService();
        String deploymentUserId = "123";
        String name = "name";
        String type = "group";
        String code = "code";
        String processDefinitionId = "multi-instance-user-task";
        String version = "1.0.2";
        String desc = "desc";

        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId(deploymentUserId);
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc(desc);
        createDeploymentCommand.setProcessDefinitionName(name);
        createDeploymentCommand.setProcessDefinitionType(type);
        createDeploymentCommand.setProcessDefinitionCode(code);
        createDeploymentCommand.setTenantId(tenantId);

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);

        Assert.assertNotNull(deploymentInstance.getInstanceId());
        Assert.assertEquals(code,deploymentInstance.getProcessDefinitionCode());
        Assert.assertEquals(type,deploymentInstance.getProcessDefinitionType());
        Assert.assertEquals(DeploymentStatusConstant.ACTIVE,deploymentInstance.getDeploymentStatus());
        Assert.assertEquals(deploymentUserId,deploymentInstance.getDeploymentUserId());
        Assert.assertEquals(LogicStatusConstant.VALID,deploymentInstance.getLogicStatus());
        Assert.assertNotNull(deploymentInstance.getProcessDefinitionContent());
        Assert.assertEquals(desc,deploymentInstance.getProcessDefinitionDesc());
        Assert.assertEquals(name,deploymentInstance.getProcessDefinitionName());
        Assert.assertEquals(version,deploymentInstance.getProcessDefinitionVersion());
        Assert.assertEquals(processDefinitionId,deploymentInstance.getProcessDefinitionId());
        Assert.assertEquals(tenantId,deploymentInstance.getTenantId());

        deploymentInstance= deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());

        Assert.assertEquals(code,deploymentInstance.getProcessDefinitionCode());
        Assert.assertEquals(type,deploymentInstance.getProcessDefinitionType());
        Assert.assertEquals(DeploymentStatusConstant.ACTIVE,deploymentInstance.getDeploymentStatus());
        Assert.assertEquals(deploymentUserId,deploymentInstance.getDeploymentUserId());
        Assert.assertEquals(LogicStatusConstant.VALID,deploymentInstance.getLogicStatus());
        Assert.assertNotNull(deploymentInstance.getProcessDefinitionContent());
        Assert.assertEquals(desc,deploymentInstance.getProcessDefinitionDesc());
        Assert.assertEquals(name,deploymentInstance.getProcessDefinitionName());
        Assert.assertEquals(version,deploymentInstance.getProcessDefinitionVersion());
        Assert.assertEquals(processDefinitionId,deploymentInstance.getProcessDefinitionId());
        Assert.assertEquals(tenantId,deploymentInstance.getTenantId());



        String newDeploymentUserId = "456";
        //String newContent = "newContent";
        String newDesc = "newDesc";
        String newName = "newName";
        String newType = "newType";

        UpdateDeploymentCommand updateDeploymentCommand = new UpdateDeploymentCommand();
        updateDeploymentCommand.setDeployInstanceId(deploymentInstance.getInstanceId());


        updateDeploymentCommand.setDeploymentUserId(newDeploymentUserId);
        //updateDeploymentCommand.setProcessDefinitionContent(newContent);
        updateDeploymentCommand.setProcessDefinitionDesc(newDesc);
        updateDeploymentCommand.setProcessDefinitionName(newName);
        updateDeploymentCommand.setProcessDefinitionType(newType);

        DeploymentInstance updatedDeploymentInstance = deploymentCommandService.updateDeployment(updateDeploymentCommand);
        Assert.assertEquals(newDeploymentUserId,updatedDeploymentInstance.getDeploymentUserId());

        //Assert.assertEquals(newContent,updatedDeploymentInstance.getProcessDefinitionContent());
        Assert.assertEquals(newDesc,updatedDeploymentInstance.getProcessDefinitionDesc());
        Assert.assertEquals(newName,updatedDeploymentInstance.getProcessDefinitionName());
        Assert.assertEquals(newType,updatedDeploymentInstance.getProcessDefinitionType());
        Assert.assertEquals(tenantId,updatedDeploymentInstance.getTenantId());


        deploymentCommandService.activateDeploymentInstance(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        DeploymentInstance activateDeploymentInstance =   deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        Assert.assertEquals(DeploymentStatusConstant.ACTIVE,activateDeploymentInstance.getDeploymentStatus());
        Assert.assertEquals(tenantId,activateDeploymentInstance.getTenantId());

        deploymentCommandService.inactivateDeploymentInstance(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        DeploymentInstance inactivateDeploymentInstance =   deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        Assert.assertEquals(DeploymentStatusConstant.INACTIVE,inactivateDeploymentInstance.getDeploymentStatus());
        Assert.assertEquals(tenantId,inactivateDeploymentInstance.getTenantId());

        deploymentCommandService.deleteDeploymentInstanceLogically(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        DeploymentInstance deleteDeploymentInstanceLogically =   deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        Assert.assertEquals(DeploymentStatusConstant.DELETED,deleteDeploymentInstanceLogically.getDeploymentStatus());
        Assert.assertEquals(LogicStatusConstant.DELETED,deleteDeploymentInstanceLogically.getLogicStatus());
        Assert.assertEquals(tenantId,deleteDeploymentInstanceLogically.getTenantId());
    }

    @Test
    public void testSimpleNoTenantId() throws Exception {

        //2.获得常用服务
        DeploymentCommandService deploymentCommandService = smartEngine.getDeploymentCommandService();
        DeploymentQueryService deploymentQueryService =  smartEngine.getDeploymentQueryService();
        String deploymentUserId = "123";
        String name = "name";
        String type = "group";
        String code = "code";
        String processDefinitionId = "multi-instance-user-task";
        String version = "1.0.2";
        String desc = "desc";

        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId(deploymentUserId);
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc(desc);
        createDeploymentCommand.setProcessDefinitionName(name);
        createDeploymentCommand.setProcessDefinitionType(type);
        createDeploymentCommand.setProcessDefinitionCode(code);
        createDeploymentCommand.setTenantId(null);

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);

        Assert.assertNotNull(deploymentInstance.getInstanceId());
        Assert.assertEquals(code,deploymentInstance.getProcessDefinitionCode());
        Assert.assertEquals(type,deploymentInstance.getProcessDefinitionType());
        Assert.assertEquals(DeploymentStatusConstant.ACTIVE,deploymentInstance.getDeploymentStatus());
        Assert.assertEquals(deploymentUserId,deploymentInstance.getDeploymentUserId());
        Assert.assertEquals(LogicStatusConstant.VALID,deploymentInstance.getLogicStatus());
        Assert.assertNotNull(deploymentInstance.getProcessDefinitionContent());
        Assert.assertEquals(desc,deploymentInstance.getProcessDefinitionDesc());
        Assert.assertEquals(name,deploymentInstance.getProcessDefinitionName());
        Assert.assertEquals(version,deploymentInstance.getProcessDefinitionVersion());
        Assert.assertEquals(processDefinitionId,deploymentInstance.getProcessDefinitionId());
        Assert.assertNull(deploymentInstance.getTenantId());

        deploymentInstance= deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());

        Assert.assertEquals(code,deploymentInstance.getProcessDefinitionCode());
        Assert.assertEquals(type,deploymentInstance.getProcessDefinitionType());
        Assert.assertEquals(DeploymentStatusConstant.ACTIVE,deploymentInstance.getDeploymentStatus());
        Assert.assertEquals(deploymentUserId,deploymentInstance.getDeploymentUserId());
        Assert.assertEquals(LogicStatusConstant.VALID,deploymentInstance.getLogicStatus());
        Assert.assertNotNull(deploymentInstance.getProcessDefinitionContent());
        Assert.assertEquals(desc,deploymentInstance.getProcessDefinitionDesc());
        Assert.assertEquals(name,deploymentInstance.getProcessDefinitionName());
        Assert.assertEquals(version,deploymentInstance.getProcessDefinitionVersion());
        Assert.assertEquals(processDefinitionId,deploymentInstance.getProcessDefinitionId());
        Assert.assertNull(deploymentInstance.getTenantId());



        String newDeploymentUserId = "456";
        //String newContent = "newContent";
        String newDesc = "newDesc";
        String newName = "newName";
        String newType = "newType";

        UpdateDeploymentCommand updateDeploymentCommand = new UpdateDeploymentCommand();
        updateDeploymentCommand.setDeployInstanceId(deploymentInstance.getInstanceId());


        updateDeploymentCommand.setDeploymentUserId(newDeploymentUserId);
        //updateDeploymentCommand.setProcessDefinitionContent(newContent);
        updateDeploymentCommand.setProcessDefinitionDesc(newDesc);
        updateDeploymentCommand.setProcessDefinitionName(newName);
        updateDeploymentCommand.setProcessDefinitionType(newType);

        DeploymentInstance updatedDeploymentInstance = deploymentCommandService.updateDeployment(updateDeploymentCommand);
        Assert.assertEquals(newDeploymentUserId,updatedDeploymentInstance.getDeploymentUserId());

        //Assert.assertEquals(newContent,updatedDeploymentInstance.getProcessDefinitionContent());
        Assert.assertEquals(newDesc,updatedDeploymentInstance.getProcessDefinitionDesc());
        Assert.assertEquals(newName,updatedDeploymentInstance.getProcessDefinitionName());
        Assert.assertEquals(newType,updatedDeploymentInstance.getProcessDefinitionType());
        Assert.assertNull(updatedDeploymentInstance.getTenantId());


        deploymentCommandService.activateDeploymentInstance(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        DeploymentInstance activateDeploymentInstance =   deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        Assert.assertEquals(DeploymentStatusConstant.ACTIVE,activateDeploymentInstance.getDeploymentStatus());
        Assert.assertNull(activateDeploymentInstance.getTenantId());

        deploymentCommandService.inactivateDeploymentInstance(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        DeploymentInstance inactivateDeploymentInstance =   deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        Assert.assertEquals(DeploymentStatusConstant.INACTIVE,inactivateDeploymentInstance.getDeploymentStatus());
        Assert.assertNull(inactivateDeploymentInstance.getTenantId());

        deploymentCommandService.deleteDeploymentInstanceLogically(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        DeploymentInstance deleteDeploymentInstanceLogically =   deploymentQueryService.findById(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        Assert.assertEquals(DeploymentStatusConstant.DELETED,deleteDeploymentInstanceLogically.getDeploymentStatus());
        Assert.assertEquals(LogicStatusConstant.DELETED,deleteDeploymentInstanceLogically.getLogicStatus());
        Assert.assertNull(deleteDeploymentInstanceLogically.getTenantId());
    }


    @Test
    public void testComplex() throws Exception {


        //2.获得常用服务
        DeploymentCommandService deploymentCommandService = smartEngine.getDeploymentCommandService();
        DeploymentQueryService deploymentQueryService =  smartEngine.getDeploymentQueryService();
        String deploymentUserId = "123";
        String name = "name";
        String type = "group";
        String code = "code";
        String processDefinitionId = "test-exclusive";
        String version = "1.0.1";
        String desc = "desc";

        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId(deploymentUserId);
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc(desc);
        createDeploymentCommand.setProcessDefinitionName(name);
        createDeploymentCommand.setProcessDefinitionType(type);
        createDeploymentCommand.setProcessDefinitionCode(code);

        DeploymentInstance deploymentInstance= deploymentCommandService.createDeployment(createDeploymentCommand);
        deploymentCommandService.createDeployment(createDeploymentCommand);
        deploymentCommandService.createDeployment(createDeploymentCommand);

        createDeploymentCommand.setProcessDefinitionType("type1");
        deploymentCommandService.createDeployment(createDeploymentCommand);
        deploymentCommandService.createDeployment(createDeploymentCommand);
        deploymentCommandService.createDeployment(createDeploymentCommand);

        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentCommandService.createDeployment(createDeploymentCommand);
        deploymentCommandService.createDeployment(createDeploymentCommand);
        deploymentCommandService.createDeployment(createDeploymentCommand);

        // 至此，属于type 数为3，type1 为6；deploymentStatus 为 active 的共有 6，inactive 的为3.


        DeploymentInstanceQueryParam deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setProcessDefinitionType("group");
        List<DeploymentInstance>  deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(3,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setProcessDefinitionType("type1");
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(6,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(3,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentUserId("123");
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(9,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentUserId("123");
        deploymentInstanceQueryParam.setPageOffset(8);
        deploymentInstanceQueryParam.setPageSize(10);
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(1,deploymentInstanceList.size());


        deploymentCommandService.deleteDeploymentInstanceLogically(deploymentInstance.getInstanceId(),deploymentInstance.getTenantId());
        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentUserId("123");
        deploymentInstanceQueryParam.setLogicStatus(LogicStatusConstant.VALID);
        deploymentInstanceQueryParam.setProcessDefinitionCode(code);
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(8,deploymentInstanceList.size());

        Integer count = deploymentQueryService.count(deploymentInstanceQueryParam);
        Assert.assertEquals(8L,count.longValue());


    }

    @Test
    public void testComplexWithTenantId() throws Exception {


        //2.获得常用服务
        DeploymentCommandService deploymentCommandService = smartEngine.getDeploymentCommandService();
        DeploymentQueryService deploymentQueryService =  smartEngine.getDeploymentQueryService();
        String deploymentUserId = "123";
        String name = "name";
        String type = "group";
        String code = "code";
        String processDefinitionId = "test-exclusive";
        String version = "1.0.1";
        String desc = "desc";
        String tenantId = "test";

        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId(deploymentUserId);
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc(desc);
        createDeploymentCommand.setProcessDefinitionName(name);
        createDeploymentCommand.setProcessDefinitionType(type);
        createDeploymentCommand.setProcessDefinitionCode(code);
        createDeploymentCommand.setTenantId(tenantId);

        DeploymentInstance deploymentInstance1 = deploymentCommandService.createDeployment(createDeploymentCommand);
        DeploymentInstance deploymentInstance2 = deploymentCommandService.createDeployment(createDeploymentCommand);
        DeploymentInstance deploymentInstance3 = deploymentCommandService.createDeployment(createDeploymentCommand);

        createDeploymentCommand.setProcessDefinitionType("type1");
        DeploymentInstance deploymentInstance4 =  deploymentCommandService.createDeployment(createDeploymentCommand);
        DeploymentInstance deploymentInstance5 = deploymentCommandService.createDeployment(createDeploymentCommand);
        DeploymentInstance deploymentInstance6 = deploymentCommandService.createDeployment(createDeploymentCommand);

        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        DeploymentInstance deploymentInstance7 = deploymentCommandService.createDeployment(createDeploymentCommand);
        DeploymentInstance deploymentInstance8 = deploymentCommandService.createDeployment(createDeploymentCommand);
        DeploymentInstance deploymentInstance9 = deploymentCommandService.createDeployment(createDeploymentCommand);

        // 至此，属于type 数为3，type1 为6；deploymentStatus 为 active 的共有 6，inactive 的为3.


        DeploymentInstanceQueryParam deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setProcessDefinitionType("group");
        List<DeploymentInstance>  deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(3,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setProcessDefinitionType("type1");
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(6,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentStatus(DeploymentStatusConstant.INACTIVE);
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(3,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentUserId("123");
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(9,deploymentInstanceList.size());

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentUserId("123");
        deploymentInstanceQueryParam.setPageOffset(8);
        deploymentInstanceQueryParam.setPageSize(10);
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(1,deploymentInstanceList.size());


        deploymentCommandService.deleteDeploymentInstanceLogically(deploymentInstance1.getInstanceId(), deploymentInstance1.getTenantId());
        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentUserId("123");
        deploymentInstanceQueryParam.setLogicStatus(LogicStatusConstant.VALID);
        deploymentInstanceQueryParam.setProcessDefinitionCode(code);
        deploymentInstanceList = deploymentQueryService.findList(deploymentInstanceQueryParam);
        Assert.assertEquals(8,deploymentInstanceList.size());

        Integer count = deploymentQueryService.count(deploymentInstanceQueryParam);
        Assert.assertEquals(8L,count.longValue());


    }




}