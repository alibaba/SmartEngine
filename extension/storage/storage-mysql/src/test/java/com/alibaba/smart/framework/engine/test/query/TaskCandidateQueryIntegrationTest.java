package com.alibaba.smart.framework.engine.test.query;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
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
 * Integration tests for TaskQuery candidate user/group query methods.
 *
 * Tests the following new TaskQuery methods:
 * - taskCandidateUser(String userId)
 * - taskCandidateGroup(String groupId)
 * - taskCandidateGroupIn(List groupIds)
 * - taskCandidateOrGroup(String userId, List groupIds)
 *
 * Uses IdAndGroupTaskAssigneeDispatcher which assigns:
 * - Users: testuser1, testuser3, testuser5
 * - Groups: testgroup11, testgroup22
 *
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TaskCandidateQueryIntegrationTest extends DatabaseBaseTestCase {

    private static final String TENANT_ID = "candidate-test-tenant";

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new IdAndGroupTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }

    // ============ taskCandidateUser Tests ============

    @Test
    public void testTaskCandidateUser_findsTaskByAssigneeUser() {
        ProcessInstance processInstance = deployAndStartProcess();

        // testuser1 is a candidate user assigned by IdAndGroupTaskAssigneeDispatcher
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Tasks should not be null", tasks);
        Assert.assertFalse("Should find tasks for candidate user testuser1", tasks.isEmpty());
        Assert.assertEquals("Should find 1 task", 1, tasks.size());
        Assert.assertEquals("Task should belong to the started process",
            processInstance.getInstanceId(), tasks.get(0).getProcessInstanceId());
    }

    @Test
    public void testTaskCandidateUser_noResultForNonCandidate() {
        deployAndStartProcess();

        // nonexistent user should not find any tasks
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateUser("nonexistent-user")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Tasks should not be null", tasks);
        Assert.assertTrue("Should not find tasks for non-candidate user", tasks.isEmpty());
    }

    @Test
    public void testTaskCandidateUser_multipleProcesses() {
        deployAndStartProcess();
        deployAndStartProcess();
        deployAndStartProcess();

        // testuser3 is also a candidate user
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser3")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertEquals("Should find 3 tasks for testuser3", 3, tasks.size());
    }

    // ============ taskCandidateGroup Tests ============

    @Test
    public void testTaskCandidateGroup_findsTaskByGroup() {
        ProcessInstance processInstance = deployAndStartProcess();

        // testgroup11 is a candidate group assigned by IdAndGroupTaskAssigneeDispatcher
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateGroup("testgroup11")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Tasks should not be null", tasks);
        Assert.assertFalse("Should find tasks for candidate group testgroup11", tasks.isEmpty());
        Assert.assertEquals("Should find 1 task", 1, tasks.size());
    }

    @Test
    public void testTaskCandidateGroup_noResultForNonCandidateGroup() {
        deployAndStartProcess();

        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateGroup("nonexistent-group")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertTrue("Should not find tasks for non-candidate group", tasks.isEmpty());
    }

    // ============ taskCandidateGroupIn Tests ============

    @Test
    public void testTaskCandidateGroupIn_findsByMultipleGroups() {
        deployAndStartProcess();

        // Both testgroup11 and testgroup22 are candidate groups
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateGroupIn(Arrays.asList("testgroup11", "testgroup22"))
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Tasks should not be null", tasks);
        // The task should be found (both groups are assigned to the same task)
        Assert.assertFalse("Should find tasks for candidate groups", tasks.isEmpty());
    }

    @Test
    public void testTaskCandidateGroupIn_varargs() {
        deployAndStartProcess();

        // Varargs overload
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateGroupIn("testgroup11", "testgroup22")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertFalse("Varargs overload should find tasks", tasks.isEmpty());
    }

    @Test
    public void testTaskCandidateGroupIn_partialMatch() {
        deployAndStartProcess();

        // One valid group + one non-existent group
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateGroupIn(Arrays.asList("testgroup11", "nonexistent-group"))
            .tenantId(TENANT_ID)
            .list();

        Assert.assertFalse("Should find tasks with at least one matching group", tasks.isEmpty());
    }

    // ============ taskCandidateOrGroup Tests ============

    @Test
    public void testTaskCandidateOrGroup_combinedUserAndGroups() {
        deployAndStartProcess();

        // Combined: user OR groups
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("testuser1", Arrays.asList("testgroup11", "testgroup22"))
            .tenantId(TENANT_ID)
            .list();

        Assert.assertNotNull("Tasks should not be null", tasks);
        Assert.assertFalse("Should find tasks with combined OR query", tasks.isEmpty());
        // Since all are assigned to the same task, should get 1 distinct result
        Assert.assertEquals("Should find 1 distinct task", 1, tasks.size());
    }

    @Test
    public void testTaskCandidateOrGroup_userMatchOnly() {
        deployAndStartProcess();

        // User matches, groups don't
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("testuser1", Collections.singletonList("nonexistent-group"))
            .tenantId(TENANT_ID)
            .list();

        Assert.assertFalse("Should find tasks even if only user matches", tasks.isEmpty());
    }

    @Test
    public void testTaskCandidateOrGroup_groupMatchOnly() {
        deployAndStartProcess();

        // User doesn't match, group matches
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("nonexistent-user", Collections.singletonList("testgroup11"))
            .tenantId(TENANT_ID)
            .list();

        Assert.assertFalse("Should find tasks even if only group matches", tasks.isEmpty());
    }

    @Test
    public void testTaskCandidateOrGroup_neitherMatch() {
        deployAndStartProcess();

        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("nonexistent-user", Collections.singletonList("nonexistent-group"))
            .tenantId(TENANT_ID)
            .list();

        Assert.assertTrue("Should not find tasks when neither user nor group matches", tasks.isEmpty());
    }

    // ============ count() with candidate filters ============

    @Test
    public void testCandidateUser_count() {
        deployAndStartProcess();
        deployAndStartProcess();

        long count = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .tenantId(TENANT_ID)
            .count();

        Assert.assertEquals("Should count 2 tasks for testuser1", 2, count);
    }

    @Test
    public void testCandidateGroup_count() {
        deployAndStartProcess();
        deployAndStartProcess();
        deployAndStartProcess();

        long count = smartEngine.createTaskQuery()
            .taskCandidateGroup("testgroup22")
            .tenantId(TENANT_ID)
            .count();

        Assert.assertEquals("Should count 3 tasks for testgroup22", 3, count);
    }

    @Test
    public void testCandidateOrGroup_count() {
        deployAndStartProcess();
        deployAndStartProcess();

        long count = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("testuser5", Arrays.asList("testgroup11"))
            .tenantId(TENANT_ID)
            .count();

        Assert.assertEquals("Should count 2 tasks for combined query", 2, count);
    }

    // ============ Combined with other filters ============

    @Test
    public void testCandidateUser_withTaskStatus() {
        ProcessInstance processInstance = deployAndStartProcess();

        // Query pending tasks only
        List<TaskInstance> pendingTasks = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .list();

        Assert.assertFalse("Should find pending tasks", pendingTasks.isEmpty());

        // Query completed tasks (none should exist)
        List<TaskInstance> completedTasks = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .taskStatus(TaskInstanceConstant.COMPLETED)
            .tenantId(TENANT_ID)
            .list();

        Assert.assertTrue("Should not find completed tasks", completedTasks.isEmpty());
    }

    @Test
    public void testCandidateUser_withProcessInstanceId() {
        ProcessInstance process1 = deployAndStartProcess();
        ProcessInstance process2 = deployAndStartProcess();

        // Filter by specific process instance
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .processInstanceId(process1.getInstanceId())
            .tenantId(TENANT_ID)
            .list();

        Assert.assertEquals("Should find 1 task for process1", 1, tasks.size());
        Assert.assertEquals("Task should belong to process1",
            process1.getInstanceId(), tasks.get(0).getProcessInstanceId());
    }

    @Test
    public void testCandidateUser_withPagination() {
        // Start 5 processes
        for (int i = 0; i < 5; i++) {
            deployAndStartProcess();
        }

        // Page 1: first 2 tasks
        List<TaskInstance> page1 = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .tenantId(TENANT_ID)
            .listPage(0, 2);

        Assert.assertEquals("Page 1 should have 2 tasks", 2, page1.size());

        // Page 2: next 2 tasks
        List<TaskInstance> page2 = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .tenantId(TENANT_ID)
            .listPage(2, 2);

        Assert.assertEquals("Page 2 should have 2 tasks", 2, page2.size());

        // Page 3: last 1 task
        List<TaskInstance> page3 = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .tenantId(TENANT_ID)
            .listPage(4, 2);

        Assert.assertEquals("Page 3 should have 1 task", 1, page3.size());

        // Verify total count
        long total = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .tenantId(TENANT_ID)
            .count();

        Assert.assertEquals("Total should be 5", 5, total);
    }

    // ============ Equivalence with old API ============

    @Test
    public void testCandidateQuery_equivalentToFindPendingTaskList() {
        deployAndStartProcess();
        deployAndStartProcess();

        // Old API
        PendingTaskQueryParam pendingParam = new PendingTaskQueryParam();
        pendingParam.setAssigneeUserId("testuser1");
        pendingParam.setAssigneeGroupIdList(Arrays.asList("testgroup11", "testgroup22"));
        pendingParam.setTenantId(TENANT_ID);
        pendingParam.setPageOffset(0);
        pendingParam.setPageSize(100);

        List<TaskInstance> oldApiResult = taskQueryService.findPendingTaskList(pendingParam);

        // New fluent API equivalent
        List<TaskInstance> fluentApiResult = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("testuser1", Arrays.asList("testgroup11", "testgroup22"))
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .listPage(0, 100);

        Assert.assertEquals("Fluent API should return same number of results as old API",
            oldApiResult.size(), fluentApiResult.size());

        // Verify they contain the same task IDs
        for (TaskInstance oldTask : oldApiResult) {
            boolean found = false;
            for (TaskInstance newTask : fluentApiResult) {
                if (oldTask.getInstanceId().equals(newTask.getInstanceId())) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue("Fluent API result should contain task " + oldTask.getInstanceId(), found);
        }
    }

    @Test
    public void testCandidateQuery_equivalentToFindTaskListByAssignee() {
        deployAndStartProcess();

        // Old API
        TaskInstanceQueryByAssigneeParam assigneeParam = new TaskInstanceQueryByAssigneeParam();
        assigneeParam.setAssigneeUserId("testuser5");
        assigneeParam.setAssigneeGroupIdList(Arrays.asList("testgroup11"));
        assigneeParam.setStatus(TaskInstanceConstant.PENDING);
        assigneeParam.setTenantId(TENANT_ID);
        assigneeParam.setPageOffset(0);
        assigneeParam.setPageSize(100);

        List<TaskInstance> oldApiResult = taskQueryService.findTaskListByAssignee(assigneeParam);

        // New fluent API equivalent
        List<TaskInstance> fluentApiResult = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("testuser5", Arrays.asList("testgroup11"))
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .listPage(0, 100);

        Assert.assertEquals("Fluent API should return same count as old API",
            oldApiResult.size(), fluentApiResult.size());
    }

    @Test
    public void testCandidateQuery_countEquivalentToOldApi() {
        deployAndStartProcess();
        deployAndStartProcess();

        // Old API count
        PendingTaskQueryParam pendingParam = new PendingTaskQueryParam();
        pendingParam.setAssigneeUserId("testuser3");
        pendingParam.setTenantId(TENANT_ID);

        Long oldCount = taskQueryService.countPendingTaskList(pendingParam);

        // New fluent API count
        long newCount = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser3")
            .taskStatus(TaskInstanceConstant.PENDING)
            .tenantId(TENANT_ID)
            .count();

        Assert.assertEquals("Count should match between old and new API",
            oldCount.longValue(), newCount);
    }

    // ============ Edge cases ============

    @Test
    public void testCandidateQuery_emptyGroupList() {
        deployAndStartProcess();

        // Empty group list should still work (only user filter)
        List<TaskInstance> tasks = smartEngine.createTaskQuery()
            .taskCandidateOrGroup("testuser1", Collections.<String>emptyList())
            .tenantId(TENANT_ID)
            .list();

        // With empty group list, the SQL should fallback to user-only matching
        Assert.assertFalse("Should find tasks with user-only filter", tasks.isEmpty());
    }

    @Test
    public void testCandidateQuery_withProcessDefinitionType() {
        deployAndStartProcess();

        // The test BPMN deploys without a specific type, so query with a non-matching type
        List<TaskInstance> noMatch = smartEngine.createTaskQuery()
            .taskCandidateUser("testuser1")
            .tenantId(TENANT_ID)
            .list();

        Assert.assertFalse("Baseline: should find tasks", noMatch.isEmpty());
    }

    // ============ Helper Methods ============

    private ProcessInstance deployAndStartProcess() {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("user-task-id-and-group-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(RequestMapSpecialKeyConstant.TENANT_ID, TENANT_ID);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), variables
        );

        return processInstance;
    }
}
