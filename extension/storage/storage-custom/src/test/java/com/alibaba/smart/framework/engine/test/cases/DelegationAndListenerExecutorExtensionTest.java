package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.configuration.ListenerExecutor;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import com.alibaba.smart.framework.engine.test.delegation.OrchestrationAdapter;
import com.alibaba.smart.framework.engine.test.delegation.OrchestrationAdapterImplement;
import com.alibaba.smart.framework.engine.util.ClassUtil;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class DelegationAndListenerExecutorExtensionTest extends CommonTestCode {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelegationAndListenerExecutorExtensionTest.class);

    @Override
    public void setUp() {
        super.setUp();


        super.processEngineConfiguration.setDelegationExecutor(new DelegationExecutor() {
            @Override
            public void execute(ExecutionContext context, Activity activity) {

                Map<String, String> properties = activity.getProperties();
                if(MapUtil.isNotEmpty(properties)){
                    String className  =  properties.get("class");
                    if(null != className){
                        OrchestrationAdapter adapter =  (OrchestrationAdapter)   ClassUtil.createOrGetInstance(className);
                        adapter.execute(null,null);
                    }else {
                        LOGGER.info("No behavior found:"+activity.getId());
                    }
                }

            }
        });



        super.processEngineConfiguration.setListenerExecutor(new ListenerExecutor() {
          @Override
          public void execute(EventConstant event, ExtensionElementContainer extensionElementContaine,
                              ExecutionContext context) {


              String eventName = event.name();

              ExtensionElements extensionElements = extensionElementContaine.getExtensionElements();
              if(null != extensionElements){

                  ListenerAggregation extension = (ListenerAggregation)extensionElements.getDecorationMap().get(
                      ExtensionElementsConstant.EXECUTION_LISTENER);

                  if(null !=  extension){
                      List<String> listenerClassNameList = extension.getEventListenerMap().get(eventName);
                      if(CollectionUtil.isNotEmpty(listenerClassNameList)){

                          for (String listenerClassName : listenerClassNameList) {

                              OrchestrationAdapter adapter =  (OrchestrationAdapter)   ClassUtil.createOrGetInstance(listenerClassName);
                              adapter.execute(null,null);
                          }
                      }
                  }


              }

          }
      });




    }

    @Test
    public void test() throws Exception {

        OrchestrationAdapterImplement.resetCounter();

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("DelegationAndListenerExecutorExtensionTest.xml").getFirstProcessDefinition();
        assertEquals(5, processDefinition.getBaseElementList().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request
        );
        Assert.assertNotNull(processInstance);

        Assert.assertTrue(processInstance.getStatus().equals(InstanceStatus.completed));

        Assert.assertEquals(2L, OrchestrationAdapterImplement.counter.get());

    }




}