package com.alibaba.smart.framework.engine.test.sharding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.UserTaskIndexDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.UserTaskIndexEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.IdAndGroupTaskAssigneeDispatcher;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * End-to-end integration tests for sharding mode.
 *
 * Verifies that enabling ShardingModeEnabledOption causes the engine to:
 * - Populate se_user_task_index when task assignees are created
 * - Route findPendingTaskList / findTaskListByAssignee to the index table
 * - Clean up index entries when tasks reach terminal status (completed/canceled)
 *
 * Uses IdAndGroupTaskAssigneeDispatcher which assigns per task:
 *   Users:  testuser1, testuser3, testuser5
 *   Groups: testgroup11, testgroup22
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ShardingModeE2ETest extends DatabaseBaseTestCase {

    private static final String TENANT_ID = "sharding-e2e-test";

    @Setter(onMethod = @__({@Autowired}))
    UserTaskIndexDAO userTaskIndexDAO;

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();

        // Enable sharding mode (add to existing OptionContainer to keep defaults)
        processEngineConfiguration.getOptionContainer().put(ConfigurationOption.SHARDING_MODE_ENABLED_OPTION);

        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new IdAndGroupTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }

    // =============================================
    // Test: Process start populates index table
    // =============================================

    @Test
    public void testStartProcess_indexTablePopulatedForAllAssignees() {
        ProcessInstance processInstance = deployAndStartProcess();

        // IdAndGroupTaskAssigneeDispatcher creates 5 assignees (3 user + 2 group)
        // Each should have an index entry in se_user_task_index

        // Verify user assignees
        assertIndexCount("testuser1", null, 1);
        assertIndexCount("testuser3", null, 1);
        assertIndexCount("testuser5", null, 1);

        // Verify group assignees
        assertIndexCountByGroup("testgroup11", 1);
        assertIndexCountByGroup("testgroup22", 1);

        // Verify index entry content
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(TENANT_ID);
        param.setAssigneeUserId("testuser1");
        List<UserTaskIndexEntity> entries = userTaskIndexDAO.findByAssignee(param);

        Assert.assertEquals(1, entries.size());
        UserTaskIndexEntity entry = entries.get(0);
        Assert.assertEquals(Long.valueOf(processInstance.getInstanceId()), entry.getProcessInstanceId());
        Assert.assertEquals(TaskInstanceConstant.PENDING, entry.getTaskStatus());
        Assert.assertEquals(TENANT_ID, entry.getTenantId());
    }

    // =============================================
    // Test: findPendingTaskList routes to index table
    // =============================================

    @Test
    public void testFindPendingTaskList_routesToIndexTable() {
        ProcessInstance processInstance = deployAndStartProcess();

        PendingTaskQueryParam param = new PendingTaskQueryParam();
        param.setAssigneeUserId("testuser1");
        param.setTenantId(TENANT_ID);
        param.setPageOffset(0);
        param.setPageSize(100);

        List<TaskInstance> tasks = taskQueryService.findPendingTaskList(param);

        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(processInstance.getInstanceId(), tasks.get(0).getProcessInstanceId());
        Assert.assertEquals(TaskInstanceConstant.PENDING, tasks.get(0).getStatus());
    }

    // =============================================
    // Test: findTaskListByAssignee with user + group
    // =============================================

    @Test
    public void testFindTaskListByAssignee_userAndGroupCombined() {
        deployAndStartProcess();

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(TENANT_ID);
        param.setAssigneeUserId("testuser1");
        param.setAssigneeGroupIdList(Arrays.asList("testgroup11", "testgroup22"));

        List<TaskInstance> tasks = taskQueryService.findTaskListByAssignee(param);

        // Should find the task (user OR group match)
        Assert.assertFalse("Should find tasks for user+group combined query", tasks.isEmpty());
    }

    @Test
    public void testFindTaskListByAssignee_groupOnly() {
        deployAndStartProcess();

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(TENANT_ID);
        param.setAssigneeGroupIdList(Arrays.asList("testgroup11"));

        List<TaskInstance> tasks = taskQueryService.findTaskListByAssignee(param);
        Assert.assertEquals(1, tasks.size());
    }

    // =============================================
    // Test: countPendingTaskList routes to index table
    // =============================================

    @Test
    public void testCountPendingTaskList_routesToIndexTable() {
        deployAndStartProcess();
        deployAndStartProcess();

        PendingTaskQueryParam param = new PendingTaskQueryParam();
        param.setAssigneeUserId("testuser1");
        param.setTenantId(TENANT_ID);

        Long count = taskQueryService.countPendingTaskList(param);
        Assert.assertEquals(Long.valueOf(2), count);
    }

    // =============================================
    // Test: Complete task → index entries cleaned up
    // =============================================

    @Test
    public void testCompleteTask_allIndexEntriesCleaned() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Verify index entries exist
        assertIndexCount("testuser1", null, 1);
        assertIndexCount("testuser3", null, 1);
        assertIndexCountByGroup("testgroup11", 1);

        // Get and complete the task
        List<TaskInstance> pendingTasks = taskQueryService.findAllPendingTaskList(
                processInstance.getInstanceId());
        Assert.assertEquals(1, pendingTasks.size());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID, "testuser1");
        taskCommandService.complete(pendingTasks.get(0).getInstanceId(), request);

        // Verify ALL index entries for this task are cleaned (all 5 assignees)
        assertIndexCount("testuser1", null, 0);
        assertIndexCount("testuser3", null, 0);
        assertIndexCount("testuser5", null, 0);
        assertIndexCountByGroup("testgroup11", 0);
        assertIndexCountByGroup("testgroup22", 0);

        // Verify process completed
        ProcessInstance finalInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.completed, finalInstance.getStatus());
    }

    @Test
    public void testCompleteTask_pendingQueryReturnsEmpty() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Complete the task
        List<TaskInstance> pendingTasks = taskQueryService.findAllPendingTaskList(
                processInstance.getInstanceId());
        Map<String, Object> request = new HashMap<String, Object>();
        request.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID, "testuser1");
        taskCommandService.complete(pendingTasks.get(0).getInstanceId(), request);

        // findPendingTaskList should return empty (goes through index table)
        PendingTaskQueryParam param = new PendingTaskQueryParam();
        param.setAssigneeUserId("testuser1");
        param.setTenantId(TENANT_ID);
        param.setPageOffset(0);
        param.setPageSize(100);

        List<TaskInstance> afterComplete = taskQueryService.findPendingTaskList(param);
        Assert.assertTrue("No pending tasks after completion", afterComplete.isEmpty());
    }

    // =============================================
    // Test: Multiple processes, complete one
    // =============================================

    @Test
    public void testMultipleProcesses_completeOne_onlyThatIndexRemoved() {
        ProcessInstance process1 = deployAndStartProcess();
        ProcessInstance process2 = deployAndStartProcess();

        // Both processes should have index entries
        assertIndexCount("testuser1", null, 2);

        // Complete task in process1
        List<TaskInstance> tasks1 = taskQueryService.findAllPendingTaskList(process1.getInstanceId());
        Map<String, Object> request = new HashMap<String, Object>();
        request.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID, "testuser1");
        taskCommandService.complete(tasks1.get(0).getInstanceId(), request);

        // Only process2's index entries should remain
        assertIndexCount("testuser1", null, 1);

        // Verify remaining entry belongs to process2
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(TENANT_ID);
        param.setAssigneeUserId("testuser1");
        List<UserTaskIndexEntity> remaining = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(Long.valueOf(process2.getInstanceId()), remaining.get(0).getProcessInstanceId());
    }

    // =============================================
    // Test: Index data consistency with main table
    // =============================================

    @Test
    public void testIndexData_consistentWithMainTable() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Get task from main table
        List<TaskInstance> mainTasks = taskQueryService.findAllPendingTaskList(
                processInstance.getInstanceId());
        Assert.assertEquals(1, mainTasks.size());
        TaskInstance mainTask = mainTasks.get(0);

        // Get from index table
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(TENANT_ID);
        param.setAssigneeUserId("testuser1");
        List<UserTaskIndexEntity> indexEntries = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(1, indexEntries.size());
        UserTaskIndexEntity indexEntry = indexEntries.get(0);

        // Verify key fields match
        Assert.assertEquals(Long.valueOf(mainTask.getInstanceId()), indexEntry.getTaskInstanceId());
        Assert.assertEquals(Long.valueOf(mainTask.getProcessInstanceId()), indexEntry.getProcessInstanceId());
        Assert.assertEquals(mainTask.getStatus(), indexEntry.getTaskStatus());
    }

    // =============================================
    // Helper methods
    // =============================================

    private ProcessInstance deployAndStartProcess() {
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("user-task-id-and-group-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(RequestMapSpecialKeyConstant.TENANT_ID, TENANT_ID);

        return processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(), variables);
    }

    private void assertIndexCount(String userId, List<String> groupIds, int expected) {
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(TENANT_ID);
        param.setAssigneeUserId(userId);
        param.setAssigneeGroupIdList(groupIds);
        Integer count = userTaskIndexDAO.countByAssignee(param);
        Assert.assertEquals("Index count mismatch for user=" + userId, Integer.valueOf(expected), count);
    }

    private void assertIndexCountByGroup(String groupId, int expected) {
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(TENANT_ID);
        param.setAssigneeGroupIdList(Arrays.asList(groupId));
        Integer count = userTaskIndexDAO.countByAssignee(param);
        Assert.assertEquals("Index count mismatch for group=" + groupId, Integer.valueOf(expected), count);
    }
}
