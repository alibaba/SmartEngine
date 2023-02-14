package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.annotation.Retryable;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultDelegationExecutor;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.util.ThreadPoolUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class RetryServiceTaskTest extends CustomBaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryServiceTaskTest.class);


    protected void initProcessConfiguration() {
        super.initProcessConfiguration();

        RetryableDelegationExecutor delegationExecutor = new RetryableDelegationExecutor();
        processEngineConfiguration.setDelegationExecutor(delegationExecutor);
        processEngineConfiguration.setExceptionProcessor(null);
    }

    class RetryableDelegationExecutor extends DefaultDelegationExecutor{
        protected void execute(ExecutionContext context, JavaDelegation delegation, ExceptionProcessor exceptionProcessor , boolean present) {


            if(!present){
                delegation.execute(context);
            }else {
                Retryable annotation = delegation.getClass().getAnnotation(Retryable.class);
                long delay = annotation.delay();
                int maxAttempts = annotation.maxAttempts();

                int attemptCount = 1;
                for (;  attemptCount <= maxAttempts; attemptCount++) {

                    boolean success = false;
                    try {
                        context.getResponse().put("count",attemptCount);

                        delegation.execute(context);
                        success = true;
                    }catch (Exception e){
                        dealException(exceptionProcessor, e,context);
                        ThreadPoolUtil.sleepSilently(delay);
                    }

                    if(success){
                        break;
                    }


                }

                if(attemptCount > maxAttempts ){
                    // means all retry failed
                    // record log ,trigger alert, persist params if needed

                    LOGGER.warn("all failed");
                }
            }
        }

        protected   void dealException(ExceptionProcessor exceptionProcessor, Exception exception,ExecutionContext context) {


        }

    }

    @Test
    public void testAllFailed() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("all-retry-servicetask.bpmn.xml").getFirstProcessDefinition();
        assertEquals(5, processDefinition.getBaseElementList().size());

        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("action","retry_all_failed");
        HashMap<String, Object> response = new HashMap<String, Object>();

        long start = System.currentTimeMillis();
        ProcessInstance processInstance =  processCommandService.start(processDefinition.getId(),processDefinition.getVersion(),request,
                response);
        long end = System.currentTimeMillis();

        long claps = end-start;

        Assert.assertTrue( claps >3000);
        Assert.assertTrue( claps <3300);

        Assert.assertEquals(response.get("count"),3);

        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());
    }


    @Test
    public void retryTwice() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("all-retry-servicetask.bpmn.xml").getFirstProcessDefinition();
        assertEquals(5, processDefinition.getBaseElementList().size());

        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("action","retry_twice");
        HashMap<String, Object> response = new HashMap<String, Object>();

        long start = System.currentTimeMillis();
        ProcessInstance processInstance =  processCommandService.start(processDefinition.getId(),processDefinition.getVersion(),request,
                response);
        long end = System.currentTimeMillis();

        long claps = end-start;

        Assert.assertTrue( claps >1020);
        Assert.assertTrue( claps <1200);

        Assert.assertEquals(response.get("count"),2);

        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());
    }

}