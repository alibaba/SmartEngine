package com.alibaba.smart.framework.engine.test.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.SupervisionConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.NotificationInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.NotificationCommandService;
import com.alibaba.smart.framework.engine.service.command.SupervisionCommandService;
import com.alibaba.smart.framework.engine.service.param.query.NotificationQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;
import com.alibaba.smart.framework.engine.service.query.NotificationQueryService;
import com.alibaba.smart.framework.engine.service.query.SupervisionQueryService;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.IdAndGroupTaskAssigneeDispatcher;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test for Supervision, Notification services and Fluent Query API.
 *
 * This test covers:
 * - SupervisionCommandService: create/close/batch supervision
 * - SupervisionQueryService: query/count supervision records
 * - NotificationCommandService: send/mark notifications
 * - NotificationQueryService: query/count notifications
 * - TaskQuery: fluent task querying
 * - ProcessInstanceQuery: fluent process querying
 * - SupervisionQuery: fluent supervision querying
 * - NotificationQuery: fluent notification querying
 *
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SupervisionNotificationFluentQueryIntegrationTest extends DatabaseBaseTestCase {

    private static final String TENANT_ID = "test-tenant-001";

    // Services under test
    private SupervisionCommandService supervisionCommandService;
    private SupervisionQueryService supervisionQueryService;
    private NotificationCommandService notificationCommandService;
    private NotificationQueryService notificationQueryService;

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new IdAndGroupTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }

    @Override
    public void setUp() {
        super.setUp();
        // Get supervision and notification services from smartEngine
        supervisionCommandService = smartEngine.getSupervisionCommandService();
        supervisionQueryService = smartEngine.getSupervisionQueryService();
        notificationCommandService = smartEngine.getNotificationCommandService();
        notificationQueryService = smartEngine.getNotificationQueryService();
    }

    // ============ SupervisionCommandService Tests ============

    @Test
    public void testCreateSupervision() {
        // Deploy and start process
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertFalse("Should have pending tasks", tasks.isEmpty());

        TaskInstance task = tasks.get(0);

        // Create supervision with valid type: urge
        SupervisionInstance supervision = supervisionCommandService.createSupervision(
            task.getInstanceId(),
            "supervisor001",
            "Task is overdue, please handle urgently",
            SupervisionConstant.SupervisionType.URGE,
            TENANT_ID
        );

        Assert.assertNotNull("Supervision should be created", supervision);
        Assert.assertNotNull("Supervision ID should not be null", supervision.getInstanceId());
        Assert.assertEquals("Supervisor should match", "supervisor001", supervision.getSupervisorUserId());
        Assert.assertEquals("Reason should match", "Task is overdue, please handle urgently", supervision.getSupervisionReason());
        Assert.assertEquals("Type should match", SupervisionConstant.SupervisionType.URGE, supervision.getSupervisionType());
        Assert.assertEquals("Status should be active", SupervisionConstant.SupervisionStatus.ACTIVE, supervision.getStatus());
    }

    @Test
    public void testCloseSupervision() {
        // Deploy and start process
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create supervision
        SupervisionInstance supervision = supervisionCommandService.createSupervision(
            task.getInstanceId(),
            "supervisor002",
            "Please expedite",
            SupervisionConstant.SupervisionType.REMIND,
            TENANT_ID
        );

        // Close supervision
        supervisionCommandService.closeSupervision(supervision.getInstanceId(), TENANT_ID);

        // Verify closed
        SupervisionInstance closedSupervision = supervisionQueryService.findOne(
            supervision.getInstanceId(), TENANT_ID);
        Assert.assertNotNull("Supervision should exist", closedSupervision);
        Assert.assertEquals("Status should be closed", SupervisionConstant.SupervisionStatus.CLOSED, closedSupervision.getStatus());
        Assert.assertNotNull("Close time should be set", closedSupervision.getCloseTime());
    }

    @Test
    public void testBatchCreateSupervision() {
        // Deploy and start multiple processes
        ProcessInstance process1 = deployAndStartProcess();
        ProcessInstance process2 = deployAndStartProcess();

        List<TaskInstance> tasks1 = taskQueryService.findAllPendingTaskList(process1.getInstanceId());
        List<TaskInstance> tasks2 = taskQueryService.findAllPendingTaskList(process2.getInstanceId());

        List<String> taskIds = Arrays.asList(
            tasks1.get(0).getInstanceId(),
            tasks2.get(0).getInstanceId()
        );

        // Batch create supervision
        List<SupervisionInstance> supervisions = supervisionCommandService.batchCreateSupervision(
            taskIds,
            "supervisor003",
            "Batch supervision for overdue tasks",
            SupervisionConstant.SupervisionType.TRACK,
            TENANT_ID
        );

        Assert.assertNotNull("Supervisions should be created", supervisions);
        Assert.assertEquals("Should have 2 supervisions", 2, supervisions.size());

        for (SupervisionInstance supervision : supervisions) {
            Assert.assertEquals("Supervisor should match", "supervisor003", supervision.getSupervisorUserId());
            Assert.assertEquals("Status should be active", SupervisionConstant.SupervisionStatus.ACTIVE, supervision.getStatus());
        }
    }

    @Test
    public void testAutoCloseSupervisionByTask() {
        // Deploy and start process
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create supervision
        SupervisionInstance supervision = supervisionCommandService.createSupervision(
            task.getInstanceId(),
            "supervisor004",
            "Auto close test",
            SupervisionConstant.SupervisionType.URGE,
            TENANT_ID
        );

        // Verify active
        Assert.assertEquals("Status should be active", SupervisionConstant.SupervisionStatus.ACTIVE, supervision.getStatus());

        // Auto close by task completion
        supervisionCommandService.autoCloseSupervisionByTask(task.getInstanceId(), TENANT_ID);

        // Verify closed
        SupervisionInstance closedSupervision = supervisionQueryService.findOne(
            supervision.getInstanceId(), TENANT_ID);
        Assert.assertEquals("Status should be closed", SupervisionConstant.SupervisionStatus.CLOSED, closedSupervision.getStatus());
    }

    // ============ SupervisionQueryService Tests ============

    @Test
    public void testFindSupervisionList() {
        // Create test data
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create multiple supervisions
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor005", "Reason 1",
            SupervisionConstant.SupervisionType.URGE, TENANT_ID);
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor006", "Reason 2",
            SupervisionConstant.SupervisionType.REMIND, TENANT_ID);

        // Query by param
        SupervisionQueryParam param = new SupervisionQueryParam();
        param.setTenantId(TENANT_ID);
        param.setTaskInstanceIdList(Collections.singletonList(task.getInstanceId()));
        param.setPageOffset(0);
        param.setPageSize(10);

        List<SupervisionInstance> supervisions = supervisionQueryService.findSupervisionList(param);
        Assert.assertNotNull("Supervisions should not be null", supervisions);
        Assert.assertEquals("Should have 2 supervisions", 2, supervisions.size());
    }

    @Test
    public void testCountSupervision() {
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create supervisions
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor007", "Reason",
            SupervisionConstant.SupervisionType.URGE, TENANT_ID);
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor008", "Reason",
            SupervisionConstant.SupervisionType.URGE, TENANT_ID);

        // Count
        SupervisionQueryParam param = new SupervisionQueryParam();
        param.setTenantId(TENANT_ID);
        param.setTaskInstanceIdList(Collections.singletonList(task.getInstanceId()));
        param.setStatus(SupervisionConstant.SupervisionStatus.ACTIVE);

        Long count = supervisionQueryService.countSupervision(param);
        Assert.assertEquals("Should have 2 active supervisions", 2L, count.longValue());
    }

    @Test
    public void testFindActiveSupervisionByTask() {
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create supervisions
        SupervisionInstance supervision1 = supervisionCommandService.createSupervision(
            task.getInstanceId(), "supervisor009", "Active", SupervisionConstant.SupervisionType.URGE, TENANT_ID);
        SupervisionInstance supervision2 = supervisionCommandService.createSupervision(
            task.getInstanceId(), "supervisor010", "Will be closed", SupervisionConstant.SupervisionType.REMIND, TENANT_ID);

        // Close one
        supervisionCommandService.closeSupervision(supervision2.getInstanceId(), TENANT_ID);

        // Find active only
        List<SupervisionInstance> activeSupervisions = supervisionQueryService.findActiveSupervisionByTask(
            task.getInstanceId(), TENANT_ID);

        Assert.assertEquals("Should have 1 active supervision", 1, activeSupervisions.size());
        Assert.assertEquals("Should be the active one", supervision1.getInstanceId(),
            activeSupervisions.get(0).getInstanceId());
    }

    // ============ NotificationCommandService Tests ============

    @Test
    public void testSendNotification() {
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Send notification to multiple receivers
        List<String> receivers = Arrays.asList("user001", "user002", "user003");

        List<NotificationInstance> notifications = notificationCommandService.sendNotification(
            processInstance.getInstanceId(),
            task.getInstanceId(),
            "sender001",
            receivers,
            "Task Notification",
            "Please review the task",
            TENANT_ID
        );

        Assert.assertNotNull("Notifications should be created", notifications);
        Assert.assertEquals("Should have 3 notifications", 3, notifications.size());

        for (NotificationInstance notification : notifications) {
            Assert.assertEquals("Process instance should match",
                processInstance.getInstanceId(), notification.getProcessInstanceId());
            Assert.assertEquals("Sender should match", "sender001", notification.getSenderUserId());
            Assert.assertEquals("Read status should be unread", NotificationConstant.ReadStatus.UNREAD, notification.getReadStatus());
        }
    }

    @Test
    public void testSendSingleNotification() {
        ProcessInstance processInstance = deployAndStartProcess();

        NotificationInstance notification = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(),
            null, // no task
            "sender002",
            "receiver001",
            "Process Started",
            "A new approval process has been started",
            NotificationConstant.NotificationType.INFORM,
            TENANT_ID
        );

        Assert.assertNotNull("Notification should be created", notification);
        Assert.assertEquals("Receiver should match", "receiver001", notification.getReceiverUserId());
        Assert.assertEquals("Type should match", NotificationConstant.NotificationType.INFORM, notification.getNotificationType());
        Assert.assertEquals("Title should match", "Process Started", notification.getTitle());
    }

    @Test
    public void testMarkAsRead() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send notification
        NotificationInstance notification = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(),
            null,
            "sender003",
            "receiver002",
            "Unread Notification",
            "Content",
            NotificationConstant.NotificationType.CC,
            TENANT_ID
        );

        Assert.assertEquals("Should be unread initially", NotificationConstant.ReadStatus.UNREAD, notification.getReadStatus());

        // Mark as read
        notificationCommandService.markAsRead(notification.getInstanceId(), TENANT_ID);

        // Verify
        NotificationInstance readNotification = notificationQueryService.findOne(
            notification.getInstanceId(), TENANT_ID);
        Assert.assertEquals("Should be read now", NotificationConstant.ReadStatus.READ, readNotification.getReadStatus());
        Assert.assertNotNull("Read time should be set", readNotification.getReadTime());
    }

    @Test
    public void testBatchMarkAsRead() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send multiple notifications
        NotificationInstance n1 = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender004", "receiver003", "N1", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        NotificationInstance n2 = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender004", "receiver003", "N2", "C2",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        NotificationInstance n3 = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender004", "receiver003", "N3", "C3",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);

        // Batch mark as read
        List<String> notificationIds = Arrays.asList(n1.getInstanceId(), n2.getInstanceId(), n3.getInstanceId());
        notificationCommandService.batchMarkAsRead(notificationIds, TENANT_ID);

        // Verify all are read
        for (String id : notificationIds) {
            NotificationInstance notification = notificationQueryService.findOne(id, TENANT_ID);
            Assert.assertEquals("Should be read", NotificationConstant.ReadStatus.READ, notification.getReadStatus());
        }
    }

    // ============ NotificationQueryService Tests ============

    @Test
    public void testFindNotificationList() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send notifications
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender005", "receiver004", "N1", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender005", "receiver004", "N2", "C2",
            NotificationConstant.NotificationType.CC, TENANT_ID);

        // Query
        NotificationQueryParam param = new NotificationQueryParam();
        param.setTenantId(TENANT_ID);
        param.setReceiverUserId("receiver004");
        param.setPageOffset(0);
        param.setPageSize(10);

        List<NotificationInstance> notifications = notificationQueryService.findNotificationList(param);
        Assert.assertNotNull("Notifications should not be null", notifications);
        Assert.assertEquals("Should have 2 notifications", 2, notifications.size());
    }

    @Test
    public void testCountUnreadNotifications() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send notifications
        NotificationInstance n1 = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender006", "receiver005", "N1", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender006", "receiver005", "N2", "C2",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender006", "receiver005", "N3", "C3",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);

        // Mark one as read
        notificationCommandService.markAsRead(n1.getInstanceId(), TENANT_ID);

        // Count unread
        Long unreadCount = notificationQueryService.countUnreadNotifications("receiver005", TENANT_ID);
        Assert.assertEquals("Should have 2 unread notifications", 2L, unreadCount.longValue());
    }

    @Test
    public void testFindByReceiver() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send to specific receiver
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender007", "receiver006", "N1", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        NotificationInstance n2 = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender007", "receiver006", "N2", "C2",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);

        // Mark one as read
        notificationCommandService.markAsRead(n2.getInstanceId(), TENANT_ID);

        // Find unread only
        List<NotificationInstance> unreadList = notificationQueryService.findByReceiver(
            "receiver006", NotificationConstant.ReadStatus.UNREAD, TENANT_ID, 0, 10);
        Assert.assertEquals("Should have 1 unread notification", 1, unreadList.size());

        // Find all
        List<NotificationInstance> allList = notificationQueryService.findByReceiver(
            "receiver006", null, TENANT_ID, 0, 10);
        Assert.assertEquals("Should have 2 notifications total", 2, allList.size());
    }

    @Test
    public void testFindBySender() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send from specific sender
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender008", "receiver007", "N1", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender008", "receiver008", "N2", "C2",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);

        // Find by sender
        List<NotificationInstance> sentList = notificationQueryService.findBySender(
            "sender008", TENANT_ID, 0, 10);
        Assert.assertEquals("Should have 2 sent notifications", 2, sentList.size());
    }

    // ============ TaskQuery (Fluent API) Tests ============

    @Test
    public void testTaskQueryBasic() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Use fluent API
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .processInstanceId(processInstance.getInstanceId())
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Tasks should not be null", tasks);
        Assert.assertFalse("Should have pending tasks", tasks.isEmpty());
    }

    @Test
    public void testTaskQueryWithOrdering() {
        // Start multiple processes to get more tasks
        ProcessInstance process1 = deployAndStartProcess();
        ProcessInstance process2 = deployAndStartProcess();

        // Use fluent API with ordering
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .processInstanceIdIn(Arrays.asList(process1.getInstanceId(), process2.getInstanceId()))
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .orderByCreateTime().desc()
            .list();

        Assert.assertNotNull("Tasks should not be null", tasks);
        Assert.assertTrue("Should have at least 2 tasks", tasks.size() >= 2);
    }

    @Test
    public void testTaskQueryWithPagination() {
        // Start multiple processes
        for (int i = 0; i < 5; i++) {
            deployAndStartProcess();
        }

        // Query first page
        List<TaskInstance> firstPage = smartEngine.createTaskQuery()
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .orderByCreateTime().desc()
            .listPage(0, 2);

        Assert.assertEquals("First page should have 2 tasks", 2, firstPage.size());

        // Query second page
        List<TaskInstance> secondPage = smartEngine.createTaskQuery()
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .orderByCreateTime().desc()
            .listPage(2, 2);

        Assert.assertEquals("Second page should have 2 tasks", 2, secondPage.size());
    }

    @Test
    public void testTaskQueryCount() {
        // Start processes
        deployAndStartProcess();
        deployAndStartProcess();
        deployAndStartProcess();

        // Count
        long count = smartEngine.createTaskQuery()
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .count();

        Assert.assertTrue("Should have at least 3 pending tasks", count >= 3);
    }

    @Test
    public void testTaskQuerySingleResult() {
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance expectedTask = tasks.get(0);

        // Single result
        TaskInstance task = smartEngine.createTaskQuery()
            .taskInstanceId(expectedTask.getInstanceId())
            .tenantId(TENANT_ID)
            .singleResult();

        Assert.assertNotNull("Task should be found", task);
        Assert.assertEquals("Task ID should match", expectedTask.getInstanceId(), task.getInstanceId());
    }

    // ============ ProcessInstanceQuery (Fluent API) Tests ============

    @Test
    public void testProcessQueryBasic() {
        ProcessInstance createdProcess = deployAndStartProcess();

        // Use fluent API
        List<ProcessInstance> processes = smartEngine.createProcessQuery()
            .processInstanceId(createdProcess.getInstanceId())
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Processes should not be null", processes);
        Assert.assertEquals("Should have 1 process", 1, processes.size());
        Assert.assertEquals("Process ID should match", createdProcess.getInstanceId(),
            processes.get(0).getInstanceId());
    }

    @Test
    public void testProcessQueryByStatus() {
        // Start multiple processes
        deployAndStartProcess();
        deployAndStartProcess();

        // Query running processes
        List<ProcessInstance> runningProcesses = smartEngine.createProcessQuery()
            .processStatus("running")
            .tenantId(TENANT_ID)
            .orderByStartTime().desc()
            .list();

        Assert.assertNotNull("Processes should not be null", runningProcesses);
        Assert.assertTrue("Should have running processes", runningProcesses.size() >= 2);

        for (ProcessInstance process : runningProcesses) {
            Assert.assertEquals("Status should be running", "running", process.getStatus().toString());
        }
    }

    @Test
    public void testProcessQueryWithPagination() {
        // Start multiple processes
        for (int i = 0; i < 5; i++) {
            deployAndStartProcess();
        }

        // Query with pagination
        List<ProcessInstance> page1 = smartEngine.createProcessQuery()
            .processStatus("running")
            .tenantId(TENANT_ID)
            .orderByStartTime().desc()
            .listPage(0, 3);

        Assert.assertEquals("Should have 3 processes", 3, page1.size());

        // Count total
        long total = smartEngine.createProcessQuery()
            .processStatus("running")
            .tenantId(TENANT_ID)
            .count();

        Assert.assertTrue("Total should be at least 5", total >= 5);
    }

    // ============ SupervisionQuery (Fluent API) Tests ============

    @Test
    public void testSupervisionQueryBasic() {
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create supervisions
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor011", "R1",
            SupervisionConstant.SupervisionType.URGE, TENANT_ID);
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor012", "R2",
            SupervisionConstant.SupervisionType.REMIND, TENANT_ID);

        // Use fluent API
        List<SupervisionInstance> supervisions = smartEngine.createSupervisionQuery()
            .taskInstanceId(task.getInstanceId())
            .supervisionStatus(SupervisionConstant.SupervisionStatus.ACTIVE)
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Supervisions should not be null", supervisions);
        Assert.assertEquals("Should have 2 supervisions", 2, supervisions.size());
    }

    @Test
    public void testSupervisionQueryBySupervisor() {
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create supervisions by specific supervisor
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor013", "R1",
            SupervisionConstant.SupervisionType.URGE, TENANT_ID);
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor013", "R2",
            SupervisionConstant.SupervisionType.REMIND, TENANT_ID);
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor014", "R3",
            SupervisionConstant.SupervisionType.URGE, TENANT_ID);

        // Query by supervisor
        List<SupervisionInstance> supervisions = smartEngine.createSupervisionQuery()
            .supervisorUserId("supervisor013")
            .tenantId(TENANT_ID)
            .orderByCreateTime().desc()
            .list();

        Assert.assertEquals("Should have 2 supervisions by supervisor013", 2, supervisions.size());
    }

    @Test
    public void testSupervisionQueryWithOrdering() {
        ProcessInstance processInstance = deployAndStartProcess();
        List<TaskInstance> tasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance task = tasks.get(0);

        // Create supervisions
        supervisionCommandService.createSupervision(task.getInstanceId(), "supervisor015", "R1",
            SupervisionConstant.SupervisionType.URGE, TENANT_ID);
        SupervisionInstance s2 = supervisionCommandService.createSupervision(
            task.getInstanceId(), "supervisor015", "R2", SupervisionConstant.SupervisionType.REMIND, TENANT_ID);

        // Close one
        supervisionCommandService.closeSupervision(s2.getInstanceId(), TENANT_ID);

        // Query with ordering by close time
        List<SupervisionInstance> closedSupervisions = smartEngine.createSupervisionQuery()
            .supervisionStatus(SupervisionConstant.SupervisionStatus.CLOSED)
            .tenantId(TENANT_ID)
            .orderByCloseTime().desc()
            .list();

        Assert.assertFalse("Should have closed supervisions", closedSupervisions.isEmpty());
    }

    // ============ NotificationQuery (Fluent API) Tests ============

    @Test
    public void testNotificationQueryBasic() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send notifications
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender009", "receiver009", "N1", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender009", "receiver009", "N2", "C2",
            NotificationConstant.NotificationType.CC, TENANT_ID);

        // Use fluent API
        List<NotificationInstance> notifications = smartEngine.createNotificationQuery()
            .receiverUserId("receiver009")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Notifications should not be null", notifications);
        Assert.assertEquals("Should have 2 notifications", 2, notifications.size());
    }

    @Test
    public void testNotificationQueryByReadStatus() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send notifications
        NotificationInstance n1 = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender010", "receiver010", "N1", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender010", "receiver010", "N2", "C2",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);

        // Mark one as read
        notificationCommandService.markAsRead(n1.getInstanceId(), TENANT_ID);

        // Query unread only
        List<NotificationInstance> unreadNotifications = smartEngine.createNotificationQuery()
            .receiverUserId("receiver010")
            .readStatus(NotificationConstant.ReadStatus.UNREAD)
            .tenantId(TENANT_ID)
            .list();

        Assert.assertEquals("Should have 1 unread notification", 1, unreadNotifications.size());
    }

    @Test
    public void testNotificationQueryByType() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send different types
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender011", "receiver011", "Inform", "C1",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);
        notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender011", "receiver011", "CC", "C2",
            NotificationConstant.NotificationType.CC, TENANT_ID);

        // Query by type
        List<NotificationInstance> informNotifications = smartEngine.createNotificationQuery()
            .receiverUserId("receiver011")
            .notificationType(NotificationConstant.NotificationType.INFORM)
            .tenantId(TENANT_ID)
            .list();

        Assert.assertEquals("Should have 1 inform notification", 1, informNotifications.size());
        Assert.assertEquals("Type should be inform", NotificationConstant.NotificationType.INFORM,
            informNotifications.get(0).getNotificationType());
    }

    @Test
    public void testNotificationQueryWithPagination() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Send many notifications
        for (int i = 0; i < 10; i++) {
            notificationCommandService.sendSingleNotification(
                processInstance.getInstanceId(), null, "sender012", "receiver012",
                "N" + i, "Content " + i, NotificationConstant.NotificationType.INFORM, TENANT_ID);
        }

        // Query with pagination
        List<NotificationInstance> page1 = smartEngine.createNotificationQuery()
            .receiverUserId("receiver012")
            .tenantId(TENANT_ID)
            .orderByCreateTime().desc()
            .listPage(0, 5);

        Assert.assertEquals("First page should have 5 notifications", 5, page1.size());

        // Count total
        long total = smartEngine.createNotificationQuery()
            .receiverUserId("receiver012")
            .tenantId(TENANT_ID)
            .count();

        Assert.assertEquals("Total should be 10", 10, total);
    }

    @Test
    public void testNotificationQuerySingleResult() {
        ProcessInstance processInstance = deployAndStartProcess();

        NotificationInstance sent = notificationCommandService.sendSingleNotification(
            processInstance.getInstanceId(), null, "sender013", "receiver013", "Single", "C",
            NotificationConstant.NotificationType.INFORM, TENANT_ID);

        // Query single result
        NotificationInstance notification = smartEngine.createNotificationQuery()
            .notificationId(sent.getInstanceId())
            .tenantId(TENANT_ID)
            .singleResult();

        Assert.assertNotNull("Notification should be found", notification);
        Assert.assertEquals("ID should match", sent.getInstanceId(), notification.getInstanceId());
    }

    // ============ Combined Scenario Tests ============

    @Test
    public void testFullWorkflowWithSupervisionAndNotification() {
        // 1. Deploy and start process
        ProcessInstance processInstance = deployAndStartProcess();
        Assert.assertNotNull("Process should be started", processInstance);

        // 2. Get pending task
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .processInstanceId(processInstance.getInstanceId())
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .list();
        Assert.assertFalse("Should have pending tasks", tasks.isEmpty());
        TaskInstance task = tasks.get(0);

        // 3. Create supervision on the task
        SupervisionInstance supervision = supervisionCommandService.createSupervision(
            task.getInstanceId(),
            "supervisor100",
            "Please handle this task urgently",
            SupervisionConstant.SupervisionType.URGE,
            TENANT_ID
        );
        Assert.assertNotNull("Supervision should be created", supervision);

        // 4. Send notification to task assignees
        List<NotificationInstance> notifications = notificationCommandService.sendNotification(
            processInstance.getInstanceId(),
            task.getInstanceId(),
            "supervisor100",
            Arrays.asList("user100", "user101"),
            "Task Supervision Notice",
            "Your task has been supervised, please handle urgently",
            TENANT_ID
        );
        Assert.assertEquals("Should send 2 notifications", 2, notifications.size());

        // 5. Verify we can query all related data using fluent API

        // Query process
        ProcessInstance queriedProcess = smartEngine.createProcessQuery()
            .processInstanceId(processInstance.getInstanceId())
            .tenantId(TENANT_ID)
            .singleResult();
        Assert.assertNotNull("Process should be found", queriedProcess);

        // Query task
        TaskInstance queriedTask = smartEngine.createTaskQuery()
            .taskInstanceId(task.getInstanceId())
            .tenantId(TENANT_ID)
            .singleResult();
        Assert.assertNotNull("Task should be found", queriedTask);

        // Query supervision
        List<SupervisionInstance> queriedSupervisions = smartEngine.createSupervisionQuery()
            .taskInstanceId(task.getInstanceId())
            .supervisionStatus(SupervisionConstant.SupervisionStatus.ACTIVE)
            .tenantId(TENANT_ID)
            .list();
        Assert.assertEquals("Should have 1 active supervision", 1, queriedSupervisions.size());

        // Query notifications for each receiver
        for (String receiver : Arrays.asList("user100", "user101")) {
            List<NotificationInstance> receiverNotifications = smartEngine.createNotificationQuery()
                .receiverUserId(receiver)
                .processInstanceId(processInstance.getInstanceId())
                .tenantId(TENANT_ID)
                .list();
            Assert.assertEquals("Each receiver should have 1 notification", 1, receiverNotifications.size());
        }

        // 6. Mark notifications as read
        for (NotificationInstance notification : notifications) {
            notificationCommandService.markAsRead(notification.getInstanceId(), TENANT_ID);
        }

        // Verify unread count is 0
        Long unreadCount = notificationQueryService.countUnreadNotifications("user100", TENANT_ID);
        Assert.assertEquals("Should have 0 unread notifications", 0L, unreadCount.longValue());

        // 7. Close supervision
        supervisionCommandService.closeSupervision(supervision.getInstanceId(), TENANT_ID);

        // Verify no active supervisions
        List<SupervisionInstance> activeSupervisions = smartEngine.createSupervisionQuery()
            .taskInstanceId(task.getInstanceId())
            .supervisionStatus(SupervisionConstant.SupervisionStatus.ACTIVE)
            .tenantId(TENANT_ID)
            .list();
        Assert.assertTrue("Should have no active supervisions", activeSupervisions.isEmpty());
    }

    // ============ Helper Methods ============

    private ProcessInstance deployAndStartProcess() {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("user-task-id-and-group-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> variables = new HashMap<>();
        variables.put(RequestMapSpecialKeyConstant.TENANT_ID, TENANT_ID);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), variables
        );

        return processInstance;
    }
}
