package com.alibaba.smart.framework.engine.test.storage.dual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageModeHolder;
import com.alibaba.smart.framework.engine.test.storage.dual.helper.BasicServiceTaskDelegation;
import com.alibaba.smart.framework.engine.test.storage.dual.helper.DefaultTaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.test.storage.dual.helper.TimeBasedIdGenerator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Dual-mode integration test: verify that DATABASE and CUSTOM storage modes
 * both work correctly within a single engine instance and a single test method.
 *
 * <p>Phase 1 (DATABASE): runs an audit process with user tasks, persisted to PostgreSQL.
 * <p>Phase 2 (CUSTOM):   runs a basic service-task process, persisted in-memory via PersisterSession.
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Service
public class DualModeProcessTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private ProcessEngineConfiguration processEngineConfiguration;
    private SmartEngine smartEngine;
    private AnnotationScanner annotationScanner;

    private RepositoryCommandService repositoryCommandService;
    private ProcessCommandService processCommandService;
    private ProcessQueryService processQueryService;
    private ExecutionQueryService executionQueryService;
    private TaskCommandService taskCommandService;
    private TaskQueryService taskQueryService;
    private ActivityQueryService activityQueryService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Before
    public void setUp() {
        annotationScanner = new SimpleAnnotationScanner(SmartEngine.class.getPackage().getName());

        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new TimeBasedIdGenerator());
        processEngineConfiguration.setInstanceAccessor(new DualModeInstanceAccessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.getOptionContainer().put(ConfigurationOption.TRANSFER_ENABLED_OPTION);

        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        repositoryCommandService = smartEngine.getRepositoryCommandService();
        processCommandService = smartEngine.getProcessCommandService();
        processQueryService = smartEngine.getProcessQueryService();
        executionQueryService = smartEngine.getExecutionQueryService();
        taskCommandService = smartEngine.getTaskCommandService();
        taskQueryService = smartEngine.getTaskQueryService();
        activityQueryService = smartEngine.getActivityQueryService();
    }

    @After
    public void tearDown() {
        StorageModeHolder.clearAll();
        if (annotationScanner != null) {
            annotationScanner.clear();
        }
    }

    /**
     * Single method that exercises BOTH storage modes in sequence,
     * proving they coexist and switch correctly within one JVM process.
     */
    @Test
    public void testBothModesInSingleMethod() throws Exception {

        // ================================================================
        // Phase 1: DATABASE MODE — Audit process with user tasks
        // ================================================================
        StorageModeHolder.set(StorageMode.DATABASE);
        try {
            // Deploy audit BPMN
            ProcessDefinition auditDef = repositoryCommandService
                .deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml")
                .getFirstProcessDefinition();
            assertEquals(17, auditDef.getBaseElementList().size());

            // Start process
            ProcessInstance auditProcess = processCommandService.start(
                auditDef.getId(), auditDef.getVersion());
            assertNotNull(auditProcess);

            // Complete submit task
            List<TaskInstance> submitTasks = taskQueryService
                .findAllPendingTaskList(auditProcess.getInstanceId());
            assertEquals(1, submitTasks.size());
            TaskInstance submitTask = submitTasks.get(0);

            Map<String, Object> submitForm = new HashMap<>();
            submitForm.put("title", "dual-mode-test");
            submitForm.put("qps", "500");
            submitForm.put("capacity", "20g");
            submitForm.put("assigneeUserId", "1");
            taskCommandService.complete(submitTask.getInstanceId(), submitForm);

            // Complete audit task (approve)
            List<TaskInstance> auditTasks = taskQueryService
                .findAllPendingTaskList(auditProcess.getInstanceId());
            assertEquals(1, auditTasks.size());
            TaskInstance auditTask = auditTasks.get(0);

            Map<String, Object> approveForm = new HashMap<>();
            approveForm.put("approve", "agree");
            approveForm.put("desc", "approved in dual-mode test");
            taskCommandService.complete(auditTask.getInstanceId(), approveForm);

            // Verify process completed
            ProcessInstance finalAuditProcess = processQueryService
                .findById(auditProcess.getInstanceId());
            assertEquals(InstanceStatus.completed, finalAuditProcess.getStatus());

            // Verify activity instances
            List<ActivityInstance> activityInstances = activityQueryService
                .findAll(auditProcess.getInstanceId());
            Assert.assertEquals(6, activityInstances.size());
            assertNotNull(activityInstances.get(0).getStartTime());

        } finally {
            StorageModeHolder.clear();
        }

        // ================================================================
        // Phase 2: CUSTOM MODE — Basic service-task process in-memory
        // ================================================================
        PersisterSession.create();
        StorageModeHolder.set(StorageMode.CUSTOM);
        try {
            BasicServiceTaskDelegation.resetCounter();

            // Deploy basic BPMN
            ProcessDefinition basicDef = repositoryCommandService
                .deploy("first-smart-editor.xml")
                .getFirstProcessDefinition();
            assertEquals(12, basicDef.getBaseElementList().size());

            // Start process (a=2 → takes the service task branch → completes)
            Map<String, Object> request = new HashMap<>();
            request.put("a", 2);

            ProcessInstance memProcess = processCommandService.start(
                basicDef.getId(), basicDef.getVersion(), request);
            assertNotNull(memProcess);
            PersisterSession.currentSession().putProcessInstance(memProcess);

            // Verify process completed
            List<ExecutionInstance> executions = executionQueryService
                .findActiveExecutionList(memProcess.getInstanceId());
            assertEquals(0, executions.size());
            assertEquals(InstanceStatus.completed, memProcess.getStatus());
            assertEquals(1L, BasicServiceTaskDelegation.getCounter().longValue());

            // Start another process (a=-1 → takes the receive task branch → stays running)
            BasicServiceTaskDelegation.resetCounter();
            Map<String, Object> request2 = new HashMap<>();
            request2.put("a", -1);

            ProcessInstance memProcess2 = processCommandService.start(
                basicDef.getId(), basicDef.getVersion(), request2);
            assertNotNull(memProcess2);
            PersisterSession.currentSession().putProcessInstance(memProcess2);

            List<ExecutionInstance> executions2 = executionQueryService
                .findActiveExecutionList(memProcess2.getInstanceId());
            assertEquals(1, executions2.size());
            assertEquals(InstanceStatus.running, memProcess2.getStatus());
            assertEquals(0L, BasicServiceTaskDelegation.getCounter().longValue());

        } finally {
            StorageModeHolder.clear();
            PersisterSession.destroySession();
        }
    }

    /**
     * Accessor that tries Spring bean lookup first (for short bean names like
     * "auditProcessServiceTaskDelegation"), then falls back to direct class
     * instantiation (for fully-qualified class names used by in-memory BPMN).
     */
    private class DualModeInstanceAccessor implements InstanceAccessor {
        @Override
        public Object access(String name) {
            try {
                return applicationContext.getBean(name);
            } catch (Exception e) {
                try {
                    return Class.forName(name).getDeclaredConstructor().newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException("Cannot resolve delegation: " + name, ex);
                }
            }
        }
    }
}
