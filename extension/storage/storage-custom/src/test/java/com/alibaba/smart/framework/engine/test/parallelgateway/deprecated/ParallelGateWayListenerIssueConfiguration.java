package com.alibaba.smart.framework.engine.test.parallelgateway.deprecated;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ListenerExecutor;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.LockException;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.Instance;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;

import org.junit.Before;

public class ParallelGateWayListenerIssueConfiguration {


    protected SmartEngine smartEngine = null;

    // 常用服务
    protected RepositoryCommandService repositoryCommandService;
    protected ProcessCommandService processCommandService;
    protected ProcessQueryService processQueryService;
    protected ExecutionQueryService executionQueryService;
    protected ExecutionCommandService executionCommandService;
    protected RepositoryQueryService repositoryQueryService;

    @Before
    public void setUp() {

        // 初始化配置
        ProcessEngineConfiguration processEngineConfiguration = initProcessEngineConfiguration();

        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        // 常用服务
        repositoryCommandService = smartEngine.getRepositoryCommandService();
        repositoryQueryService = smartEngine.getRepositoryQueryService();
        processCommandService = smartEngine.getProcessCommandService();
        processQueryService = smartEngine.getProcessQueryService();
        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();

    }

    public ProcessEngineConfiguration initProcessEngineConfiguration() {

        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setInstanceAccessor(new TestInstanceAccessService());
        processEngineConfiguration.setLockStrategy(new TestLockStrategy());
        processEngineConfiguration.setIdGenerator(new TestIdGenerator());
        processEngineConfiguration.setListenerExecutor(new TestListenerExecutor());

        return processEngineConfiguration;
    }

    public class TestInstanceAccessService implements InstanceAccessor {

        @Override
        public Object access(String s) {
            Object obj = null;
            try {
                Class clz = Class.forName(s);
                obj = clz.newInstance();
                return obj;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return obj;
        }
    }

    public class TestIdGenerator implements IdGenerator {

        @Override
        synchronized public void generate(Instance instance) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String s = String.valueOf(System.currentTimeMillis());
            instance.setInstanceId(s);
//            return s;
        }
    }

    public class TestListenerExecutor implements ListenerExecutor {

        public void execute(EventConstant event, ExtensionElementContainer extensionElementContainer,
                            ExecutionContext context) {
            String eventName = event.name();

            ActivityInstance activityInstance = context.getActivityInstance();

            if(extensionElementContainer instanceof IdBasedElement){
                System.out.printf("[%s][%s][%s]\n",
                    new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()))
                    ,((IdBasedElement) extensionElementContainer).getId(),
                    eventName
                );
                //System.out.printf("%s\n\n", InstanceSerializerFacade.serialize(context.getProcessInstance()));
            }else {
                System.out.printf("[%s][%s][%s]\n",
                    new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()))
                    , extensionElementContainer,
                    eventName
                );
                System.out.printf("%s abc     \n\n", InstanceSerializerFacade.serialize(context.getProcessInstance()));
            }






            ExtensionElements extensionElements = extensionElementContainer.getExtensionElements();
            if (null != extensionElements) {

                ListenerAggregation extension = (ListenerAggregation)extensionElements.getDecorationMap().get(
                    ExtensionElementsConstant.EXECUTION_LISTENER);

                if (null != extension) {
                    List<String> listenerClassNameList = extension.getEventListenerMap().get(eventName);
                    if (CollectionUtil.isNotEmpty(listenerClassNameList)) {
                        InstanceAccessor instanceAccessor = context.getProcessEngineConfiguration()
                            .getInstanceAccessor();
                        for (String listenerClassName : listenerClassNameList) {

                            Listener listener = (Listener)instanceAccessor.access(listenerClassName);
                            listener.execute(event, context);
                        }
                    }
                }

            }

        }
    }

    public class TestLockStrategy implements LockStrategy {

        private ConcurrentHashMap<String, Object> lock = new ConcurrentHashMap<String, Object>();

        @Override
        public void tryLock(String processInstanceId, ExecutionContext executionContext) throws LockException {
            if (executionContext == null) {
                return;
            }
            if (lock.contains(processInstanceId)) {
                throw new LockException("[Test lock] key already exists, " + processInstanceId);
            }
            lock.put(processInstanceId, processInstanceId);
        }

        @Override
        public void unLock(String s, ExecutionContext executionContext) throws LockException {
            lock.remove(s);

        }
    }

}