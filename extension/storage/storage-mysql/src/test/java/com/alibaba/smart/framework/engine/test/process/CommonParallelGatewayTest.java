package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.ParallelGatewayHelper;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
/**
 *  场景1:不嵌套, 从fork开始,直接都进入join节点(中间节点不暂停,都是serviceTask), 验证流程实例状态,流转轨迹状态,中间的bean执行逻辑,join逻辑生效(都在此等待,并且后续节点只会被执行一次)
 *  场景2:不嵌套, 从fork开始,分支1进入join节点,分支2进入receiveTask, 验证流程实例状态,流转轨迹状态,中间的bean执行逻辑,join逻辑生效(都到齐了再触发,并且后续节点只会被执行一次)
 *  场景3:不嵌套, 从fork开始,分支1,分支2进入receiveTask,然后先后驱动流程到结束. 验证流程实例状态,流转轨迹状态,中间的bean执行逻辑,join逻辑生效(都到齐了再触发,并且后续节点只会被执行一次)
 *  场景4:嵌套, 主fork下3个子fork,这3个子fork分别模拟上面的场景1,2,3
 *  场景5:嵌套, 主fork下3个子fork,2个子fork先join后,然后再和最后一个子fork相join.
 */
public class CommonParallelGatewayTest extends DatabaseBaseTestCase {

    protected void initProcessConfiguration() {

        super.initProcessConfiguration();

        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());

        //指定线程池,多线程fork
        processEngineConfiguration.setExecutorService( Executors.newFixedThreadPool(10));

    }

    @Test
    public void testNonEmbedded()  {

        //验证场景1 ,没有嵌套

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/ParallelGatewayAllServiceTaskTest.xml").getFirstProcessDefinition();
        assertEquals(16, processDefinition.getBaseElementList().size());

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinition.getId(),
                        processDefinition.getVersion());

        Map<String, String> matchedJoinGateway = ParallelGatewayHelper.findMatchedJoinGateway(pvmProcessDefinition);

        String join = matchedJoinGateway.get("fork");
        Assert.assertEquals("join",join);

    }

    @Test
    public void testSimpleEmbedded()  {

        //验证场景1 ,没有嵌套

        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/SimpleEmbeddedParallelGateway.xml").getFirstProcessDefinition();
        assertEquals(22, processDefinition.getBaseElementList().size());

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinition.getId(),
                        processDefinition.getVersion());

        Map<String, String> matchedJoinGateway = ParallelGatewayHelper.findMatchedJoinGateway(pvmProcessDefinition);

        String join = matchedJoinGateway.get("parentFork");
        Assert.assertEquals("parentJoin",join);

        join = matchedJoinGateway.get("childFork");
        Assert.assertEquals("childJoin",join);

    }


    @Test
    public void testComplexEmbedded()  {

        //验证场景1 ,没有嵌套

        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/ComplexEmbeddedParallelGateway.xml").getFirstProcessDefinition();
        assertEquals(21, processDefinition.getBaseElementList().size());

        PvmProcessDefinition pvmProcessDefinition = smartEngine.getProcessEngineConfiguration()
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processDefinition.getId(),
                        processDefinition.getVersion());

        Map<String, String> matchedJoinGateway = ParallelGatewayHelper.findMatchedJoinGateway(pvmProcessDefinition);

        String join = matchedJoinGateway.get("parentFork");
        Assert.assertEquals("parentJoin",join);

        join = matchedJoinGateway.get("subFork");
        Assert.assertEquals("subJoin",join);

    }



}