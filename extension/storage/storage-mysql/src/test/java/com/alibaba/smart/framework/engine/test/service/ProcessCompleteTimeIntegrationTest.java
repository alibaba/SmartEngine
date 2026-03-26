package com.alibaba.smart.framework.engine.test.service;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.BaseElementTest;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Process complete time and query filter integration test
 *
 * @author SmartEngine Team
 */
public class ProcessCompleteTimeIntegrationTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    private ProcessInstanceDAO processInstanceDAO;

    @Setter(onMethod = @__({@Autowired}))
    private TaskInstanceDAO taskInstanceDAO;

    private static final String TENANT_ID = "-3";

    // ID counters for unique IDs
    private long processIdCounter = 6001L;
    private long taskIdCounter = 7001L;

    @Test
    public void testProcessCompleteTimeIsSet() {
        // Create a completed process instance
        ProcessInstanceEntity process = createProcessInstance("completed", processIdCounter++);

        // Set complete time
        Date completeTime = new Date();
        process.setCompleteTime(completeTime);

        // Update process
        processInstanceDAO.update(process);

        // Query and verify
        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(process.getId(), TENANT_ID);
        Assert.assertNotNull("Process should exist", retrieved);
        Assert.assertNotNull("Complete time should be set", retrieved.getCompleteTime());
        Assert.assertEquals("Status should be completed", "completed", retrieved.getStatus());
    }

    @Test
    public void testQueryCompletedProcessByTimeRange() {
        Calendar cal = Calendar.getInstance();

        // Process1: completed 3 days ago
        cal.add(Calendar.DAY_OF_MONTH, -3);
        Date threeDaysAgo = cal.getTime();
        ProcessInstanceEntity process1 = createProcessInstance("completed", processIdCounter++);
        process1.setCompleteTime(threeDaysAgo);
        processInstanceDAO.update(process1);

        // Process2: completed 2 days ago
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        Date twoDaysAgo = cal.getTime();
        ProcessInstanceEntity process2 = createProcessInstance("completed", processIdCounter++);
        process2.setCompleteTime(twoDaysAgo);
        processInstanceDAO.update(process2);

        // Process3: completed 1 day ago
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayAgo = cal.getTime();
        ProcessInstanceEntity process3 = createProcessInstance("completed", processIdCounter++);
        process3.setCompleteTime(oneDayAgo);
        processInstanceDAO.update(process3);

        // Process4: running (no complete time)
        ProcessInstanceEntity process4 = createProcessInstance("running", processIdCounter++);

        // Query: processes completed between 2.5 days ago and 0.5 days ago
        ProcessInstanceQueryParam queryParam = new ProcessInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        cal.add(Calendar.HOUR, -12);
        queryParam.setCompleteTimeStart(cal.getTime());

        cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -12);
        queryParam.setCompleteTimeEnd(cal.getTime());

        List<ProcessInstanceEntity> results = processInstanceDAO.find(queryParam);

        Assert.assertNotNull("Results should not be null", results);
        Assert.assertTrue("Should have at least 2 results", results.size() >= 2);

        // Verify all returned processes are completed
        for (ProcessInstanceEntity result : results) {
            Assert.assertEquals("Status should be completed", "completed", result.getStatus());
            Assert.assertNotNull("Complete time should not be null", result.getCompleteTime());
        }
    }

    @Test
    public void testTaskCompleteTimeFiltering() {
        Calendar cal = Calendar.getInstance();

        // Task1: completed 2 days ago
        cal.add(Calendar.DAY_OF_MONTH, -2);
        Date twoDaysAgo = cal.getTime();
        TaskInstanceEntity task1 = createTaskInstance("completed", taskIdCounter++);
        task1.setCompleteTime(twoDaysAgo);
        taskInstanceDAO.update(task1);

        // Task2: completed 1 day ago
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayAgo = cal.getTime();
        TaskInstanceEntity task2 = createTaskInstance("completed", taskIdCounter++);
        task2.setCompleteTime(oneDayAgo);
        taskInstanceDAO.update(task2);

        // Task3: running
        TaskInstanceEntity task3 = createTaskInstance("running", taskIdCounter++);

        // Query: tasks completed within last 1.5 days
        TaskInstanceQueryParam queryParam = new TaskInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.add(Calendar.HOUR, -12);
        queryParam.setCompleteTimeStart(cal.getTime());

        queryParam.setCompleteTimeEnd(new Date());

        List<TaskInstanceEntity> results = taskInstanceDAO.findTaskList(queryParam);

        Assert.assertNotNull("Results should not be null", results);

        // Verify returned tasks are completed and within time range
        for (TaskInstanceEntity result : results) {
            Assert.assertEquals("Status should be completed", "completed", result.getStatus());
            Assert.assertNotNull("Complete time should not be null", result.getCompleteTime());
        }
    }

    @Test
    public void testRunningProcessHasNoCompleteTime() {
        ProcessInstanceEntity runningProcess = createProcessInstance("running", processIdCounter++);

        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(runningProcess.getId(), TENANT_ID);
        Assert.assertNotNull("Process should exist", retrieved);
        Assert.assertEquals("Status should be running", "running", retrieved.getStatus());
        Assert.assertNull("Complete time should be null for running process", retrieved.getCompleteTime());
    }

    @Test
    public void testCompleteTimeSetWhenProcessCompletes() {
        // Simulate process from running to completed
        ProcessInstanceEntity process = createProcessInstance("running", processIdCounter++);
        Assert.assertNull("Initial complete time should be null", process.getCompleteTime());

        // Process completes
        process.setStatus("completed");
        process.setCompleteTime(new Date());
        processInstanceDAO.update(process);

        // Verify
        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(process.getId(), TENANT_ID);
        Assert.assertEquals("Status should be completed", "completed", retrieved.getStatus());
        Assert.assertNotNull("Complete time should be set", retrieved.getCompleteTime());

        // Verify complete time is recent (within last minute)
        long timeDiff = new Date().getTime() - retrieved.getCompleteTime().getTime();
        Assert.assertTrue("Complete time should be recent", timeDiff < 60000);
    }

    @Test
    public void testQueryOnlyCompletedProcessesInTimeRange() {
        Calendar cal = Calendar.getInstance();

        // 1. Completed yesterday
        cal.add(Calendar.DAY_OF_MONTH, -1);
        ProcessInstanceEntity completed1 = createProcessInstance("completed", processIdCounter++);
        completed1.setCompleteTime(cal.getTime());
        processInstanceDAO.update(completed1);

        // 2. Completed today
        ProcessInstanceEntity completed2 = createProcessInstance("completed", processIdCounter++);
        completed2.setCompleteTime(new Date());
        processInstanceDAO.update(completed2);

        // 3. Running (no complete time)
        ProcessInstanceEntity running = createProcessInstance("running", processIdCounter++);

        // 4. Cancelled (has complete time)
        ProcessInstanceEntity cancelled = createProcessInstance("cancelled", processIdCounter++);
        cancelled.setCompleteTime(new Date());
        processInstanceDAO.update(cancelled);

        // Query: completed processes in last 2 days
        ProcessInstanceQueryParam queryParam = new ProcessInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        queryParam.setCompleteTimeStart(cal.getTime());
        queryParam.setCompleteTimeEnd(new Date());

        List<ProcessInstanceEntity> results = processInstanceDAO.find(queryParam);

        Assert.assertNotNull("Results should not be null", results);

        // Verify: all results are completed status
        for (ProcessInstanceEntity result : results) {
            Assert.assertEquals("All results should have completed status", "completed", result.getStatus());
            Assert.assertNotNull("All results should have complete time", result.getCompleteTime());
        }
    }

    @Test
    public void testHistoricalDataWithNullCompleteTime() {
        // Simulate historical data: completed but complete_time is null
        ProcessInstanceEntity historicalProcess = createProcessInstance("completed", processIdCounter++);
        // Don't set completeTime, keep it null

        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(historicalProcess.getId(), TENANT_ID);
        Assert.assertEquals("Status should be completed", "completed", retrieved.getStatus());

        // Query should handle null values
        ProcessInstanceQueryParam queryParam = new ProcessInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        queryParam.setCompleteTimeStart(cal.getTime());
        queryParam.setCompleteTimeEnd(new Date());

        // Query should not crash even with null complete time records
        List<ProcessInstanceEntity> results = processInstanceDAO.find(queryParam);
        Assert.assertNotNull("Results should not be null even with historical data", results);
    }

    // Helper method: create test process instance
    private ProcessInstanceEntity createProcessInstance(String status, long id) {
        ProcessInstanceEntity entity = new ProcessInstanceEntity();
        entity.setId(id);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessDefinitionIdAndVersion("testProcess:1");
        entity.setProcessDefinitionType("bpmn20");
        entity.setStatus(status);
        entity.setTitle("Test Process");
        entity.setTenantId(TENANT_ID);

        processInstanceDAO.insert(entity);
        return entity;
    }

    // Helper method: create test task instance
    private TaskInstanceEntity createTaskInstance(String status, long id) {
        TaskInstanceEntity entity = new TaskInstanceEntity();
        entity.setId(id);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessInstanceId(1000L);
        entity.setExecutionInstanceId(2000L);
        entity.setActivityInstanceId(3000L);
        entity.setProcessDefinitionIdAndVersion("testProcess:1");
        entity.setProcessDefinitionActivityId("testTask");
        entity.setTitle("Test Task");
        entity.setStatus(status);
        entity.setTenantId(TENANT_ID);

        taskInstanceDAO.insert(entity);
        return entity;
    }
}
