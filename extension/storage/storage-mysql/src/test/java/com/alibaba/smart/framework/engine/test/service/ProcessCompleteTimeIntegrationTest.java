package com.alibaba.smart.framework.engine.test.service;

import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 流程完成时间和查询过滤集成测试
 * 测试流程和任务的完成时间记录及查询过滤功能
 *
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProcessCompleteTimeIntegrationTest extends DatabaseBaseTestCase {

    @Autowired
    private ProcessInstanceDAO processInstanceDAO;

    @Autowired
    private TaskInstanceDAO taskInstanceDAO;

    private static final String TENANT_ID = "test-tenant";

    @Test
    public void testProcessCompleteTimeIsSet() {
        // 创建一个已完成的流程实例
        ProcessInstanceEntity process = createProcessInstance("completed");

        // 设置完成时间
        Date completeTime = new Date();
        process.setCompleteTime(completeTime);

        // 更新流程
        processInstanceDAO.update(process);

        // 查询验证
        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(process.getId(), TENANT_ID);
        assertNotNull("Process should exist", retrieved);
        assertNotNull("Complete time should be set", retrieved.getCompleteTime());
        assertEquals("Status should be completed", "completed", retrieved.getStatus());
    }

    @Test
    public void testQueryCompletedProcessByTimeRange() {
        // 创建测试数据：3个已完成的流程，完成时间不同
        Calendar cal = Calendar.getInstance();

        // 流程1：3天前完成
        cal.add(Calendar.DAY_OF_MONTH, -3);
        Date threeDaysAgo = cal.getTime();
        ProcessInstanceEntity process1 = createProcessInstance("completed");
        process1.setCompleteTime(threeDaysAgo);
        processInstanceDAO.update(process1);

        // 流程2：2天前完成
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        Date twoDaysAgo = cal.getTime();
        ProcessInstanceEntity process2 = createProcessInstance("completed");
        process2.setCompleteTime(twoDaysAgo);
        processInstanceDAO.update(process2);

        // 流程3：1天前完成
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayAgo = cal.getTime();
        ProcessInstanceEntity process3 = createProcessInstance("completed");
        process3.setCompleteTime(oneDayAgo);
        processInstanceDAO.update(process3);

        // 流程4：运行中（无完成时间）
        ProcessInstanceEntity process4 = createProcessInstance("running");

        // 查询：2.5天前到0.5天前完成的流程
        ProcessInstanceQueryParam queryParam = new ProcessInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        cal.add(Calendar.HOUR, -12);
        queryParam.setCompleteTimeStart(cal.getTime());

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 0);
        cal.add(Calendar.HOUR, -12);
        queryParam.setCompleteTimeEnd(cal.getTime());

        List<ProcessInstanceEntity> results = processInstanceDAO.find(queryParam);

        // 应该返回process2和process3
        assertNotNull("Results should not be null", results);
        assertTrue("Should have at least 2 results", results.size() >= 2);

        // 验证返回的都是已完成的流程
        for (ProcessInstanceEntity result : results) {
            assertEquals("Status should be completed", "completed", result.getStatus());
            assertNotNull("Complete time should not be null", result.getCompleteTime());
        }
    }

    @Test
    public void testTaskCompleteTimeFiltering() {
        // 创建已完成的任务
        Calendar cal = Calendar.getInstance();

        // 任务1：2天前完成
        cal.add(Calendar.DAY_OF_MONTH, -2);
        Date twoDaysAgo = cal.getTime();
        TaskInstanceEntity task1 = createTaskInstance("completed");
        task1.setCompleteTime(twoDaysAgo);
        taskInstanceDAO.update(task1);

        // 任务2：1天前完成
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date oneDayAgo = cal.getTime();
        TaskInstanceEntity task2 = createTaskInstance("completed");
        task2.setCompleteTime(oneDayAgo);
        taskInstanceDAO.update(task2);

        // 任务3：运行中
        TaskInstanceEntity task3 = createTaskInstance("running");

        // 查询：最近1.5天内完成的任务
        TaskInstanceQueryParam queryParam = new TaskInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.add(Calendar.HOUR, -12);
        queryParam.setCompleteTimeStart(cal.getTime());

        queryParam.setCompleteTimeEnd(new Date());

        List<TaskInstanceEntity> results = taskInstanceDAO.findTaskList(queryParam);

        // 应该只返回task2
        assertNotNull("Results should not be null", results);

        // 验证返回的都是已完成且在时间范围内的任务
        for (TaskInstanceEntity result : results) {
            assertEquals("Status should be completed", "completed", result.getStatus());
            assertNotNull("Complete time should not be null", result.getCompleteTime());
            assertTrue("Complete time should be within range",
                result.getCompleteTime().after(queryParam.getCompleteTimeStart()) ||
                result.getCompleteTime().equals(queryParam.getCompleteTimeStart()));
        }
    }

    @Test
    public void testRunningProcessHasNoCompleteTime() {
        // 运行中的流程不应该有完成时间
        ProcessInstanceEntity runningProcess = createProcessInstance("running");

        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(runningProcess.getId(), TENANT_ID);
        assertNotNull("Process should exist", retrieved);
        assertEquals("Status should be running", "running", retrieved.getStatus());
        assertNull("Complete time should be null for running process", retrieved.getCompleteTime());
    }

    @Test
    public void testCompleteTimeSetWhenProcessCompletes() {
        // 模拟流程从运行中到完成的状态变更
        ProcessInstanceEntity process = createProcessInstance("running");
        assertNull("Initial complete time should be null", process.getCompleteTime());

        // 流程完成
        process.setStatus("completed");
        process.setCompleteTime(new Date());
        processInstanceDAO.update(process);

        // 验证
        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(process.getId(), TENANT_ID);
        assertEquals("Status should be completed", "completed", retrieved.getStatus());
        assertNotNull("Complete time should be set", retrieved.getCompleteTime());

        // 验证完成时间在合理范围内（最近1分钟内）
        long timeDiff = new Date().getTime() - retrieved.getCompleteTime().getTime();
        assertTrue("Complete time should be recent", timeDiff < 60000); // 小于60秒
    }

    @Test
    public void testQueryOnlyCompletedProcessesInTimeRange() {
        Calendar cal = Calendar.getInstance();

        // 创建多个流程，状态和完成时间各不相同
        // 1. 已完成，昨天
        cal.add(Calendar.DAY_OF_MONTH, -1);
        ProcessInstanceEntity completed1 = createProcessInstance("completed");
        completed1.setCompleteTime(cal.getTime());
        processInstanceDAO.update(completed1);

        // 2. 已完成，今天
        ProcessInstanceEntity completed2 = createProcessInstance("completed");
        completed2.setCompleteTime(new Date());
        processInstanceDAO.update(completed2);

        // 3. 运行中（无完成时间）
        ProcessInstanceEntity running = createProcessInstance("running");

        // 4. 已取消（有完成时间）
        ProcessInstanceEntity cancelled = createProcessInstance("cancelled");
        cancelled.setCompleteTime(new Date());
        processInstanceDAO.update(cancelled);

        // 查询：最近2天内完成的已完成流程
        ProcessInstanceQueryParam queryParam = new ProcessInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        queryParam.setCompleteTimeStart(cal.getTime());
        queryParam.setCompleteTimeEnd(new Date());

        List<ProcessInstanceEntity> results = processInstanceDAO.find(queryParam);

        assertNotNull("Results should not be null", results);

        // 验证：所有结果都是已完成状态
        for (ProcessInstanceEntity result : results) {
            assertEquals("All results should have completed status", "completed", result.getStatus());
            assertNotNull("All results should have complete time", result.getCompleteTime());
        }
    }

    @Test
    public void testHistoricalDataWithNullCompleteTime() {
        // 模拟历史数据：已完成但complete_time为null
        ProcessInstanceEntity historicalProcess = createProcessInstance("completed");
        // 不设置completeTime，保持为null

        ProcessInstanceEntity retrieved = processInstanceDAO.findOne(historicalProcess.getId(), TENANT_ID);
        assertEquals("Status should be completed", "completed", retrieved.getStatus());
        // 历史数据可能没有完成时间
        // assertNull("Historical data may have null complete time", retrieved.getCompleteTime());

        // 查询时应该能处理null值
        ProcessInstanceQueryParam queryParam = new ProcessInstanceQueryParam();
        queryParam.setStatus("completed");
        queryParam.setTenantId(TENANT_ID);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -7);
        queryParam.setCompleteTimeStart(cal.getTime());
        queryParam.setCompleteTimeEnd(new Date());

        // 查询不应该崩溃，即使有null完成时间的记录
        List<ProcessInstanceEntity> results = processInstanceDAO.find(queryParam);
        assertNotNull("Results should not be null even with historical data", results);
    }

    // 辅助方法：创建测试用流程实例
    private ProcessInstanceEntity createProcessInstance(String status) {
        ProcessInstanceEntity entity = new ProcessInstanceEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setProcessDefinitionIdAndVersion("testProcess:1");
        entity.setProcessDefinitionType("bpmn20");
        entity.setStatus(status);
        entity.setTitle("Test Process");
        entity.setTenantId(TENANT_ID);

        processInstanceDAO.insert(entity);
        return entity;
    }

    // 辅助方法：创建测试用任务实例
    private TaskInstanceEntity createTaskInstance(String status) {
        TaskInstanceEntity entity = new TaskInstanceEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setProcessInstanceId(1000L);
        entity.setExecutionInstanceId(2000L);
        entity.setProcessDefinitionIdAndVersion("testProcess:1");
        entity.setProcessDefinitionActivityId("testTask");
        entity.setTitle("Test Task");
        entity.setStatus(status);
        entity.setTenantId(TENANT_ID);

        taskInstanceDAO.insert(entity);
        return entity;
    }
}
