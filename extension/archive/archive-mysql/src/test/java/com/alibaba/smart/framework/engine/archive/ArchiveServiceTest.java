package com.alibaba.smart.framework.engine.archive;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.archive.config.ArchiveProperties;
import com.alibaba.smart.framework.engine.archive.dao.ArchiveDAO;
import com.alibaba.smart.framework.engine.archive.service.ArchiveService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/archive-test.xml")
public class ArchiveServiceTest {

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private ArchiveProperties archiveProperties;

    @Autowired
    private ArchiveDAO archiveDAO;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        cleanupTestData();
    }

    private void cleanupTestData() {
        // Clean archive tables
        jdbcTemplate.execute("DELETE FROM se_task_transfer_record_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_assignee_operation_record_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_supervision_instance_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_notification_instance_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_process_rollback_record_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_task_assignee_instance_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_variable_instance_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_task_instance_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_execution_instance_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_activity_instance_archive WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_process_instance_archive WHERE tenant_id = 'archive_test'");

        // Clean runtime tables
        jdbcTemplate.execute("DELETE FROM se_task_transfer_record WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_assignee_operation_record WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_supervision_instance WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_notification_instance WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_process_rollback_record WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_task_assignee_instance WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_variable_instance WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_task_instance WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_execution_instance WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_activity_instance WHERE tenant_id = 'archive_test'");
        jdbcTemplate.execute("DELETE FROM se_process_instance WHERE tenant_id = 'archive_test'");
    }

    @Test
    public void testArchiveDisabled() {
        archiveProperties.setEnabled(false);
        try {
            int count = archiveService.archive("archive_test");
            Assert.assertEquals(0, count);
        } finally {
            archiveProperties.setEnabled(true);
        }
    }

    @Test
    public void testArchiveCompletedProcess() {
        // Insert a completed process older than retention period
        long processId = insertCompletedProcess(daysAgo(5));
        insertRelatedData(processId);

        archiveProperties.setRetentionDays(1);
        int count = archiveService.archive("archive_test");

        Assert.assertEquals(1, count);

        // Verify archived
        Long archivedCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM se_process_instance_archive WHERE id = ? AND tenant_id = 'archive_test'",
                Long.class, processId);
        Assert.assertEquals(Long.valueOf(1), archivedCount);

        // Verify original deleted
        Long originalCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM se_process_instance WHERE id = ? AND tenant_id = 'archive_test'",
                Long.class, processId);
        Assert.assertEquals(Long.valueOf(0), originalCount);
    }

    @Test
    public void testRunningProcessNotArchived() {
        // Insert a running process
        long processId = insertProcess("running", daysAgo(5), null);

        archiveProperties.setRetentionDays(1);
        int count = archiveService.archive("archive_test");

        Assert.assertEquals(0, count);

        // Verify still in original table
        Long originalCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM se_process_instance WHERE id = ? AND tenant_id = 'archive_test'",
                Long.class, processId);
        Assert.assertEquals(Long.valueOf(1), originalCount);
    }

    @Test
    public void testRecentCompletedNotArchived() {
        // Insert a recently completed process (within retention period)
        long processId = insertCompletedProcess(new Date());

        archiveProperties.setRetentionDays(30);
        int count = archiveService.archive("archive_test");

        Assert.assertEquals(0, count);

        // Verify still in original table
        Long originalCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM se_process_instance WHERE id = ? AND tenant_id = 'archive_test'",
                Long.class, processId);
        Assert.assertEquals(Long.valueOf(1), originalCount);
    }

    @Test
    public void testBatchArchive() {
        // Insert multiple completed processes older than retention
        for (int i = 0; i < 5; i++) {
            long processId = insertCompletedProcess(daysAgo(10));
            insertRelatedData(processId);
        }

        archiveProperties.setRetentionDays(1);
        archiveProperties.setBatchSize(2);
        int count = archiveService.archive("archive_test");

        Assert.assertEquals(5, count);

        Long archivedCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM se_process_instance_archive WHERE tenant_id = 'archive_test'",
                Long.class);
        Assert.assertEquals(Long.valueOf(5), archivedCount);
    }

    @Test
    public void testRelatedDataArchived() {
        long processId = insertCompletedProcess(daysAgo(5));
        insertRelatedData(processId);

        archiveProperties.setRetentionDays(1);
        archiveService.archive("archive_test");

        // Verify all related tables have archived data
        assertArchivedCount("se_task_instance_archive", "process_instance_id", processId, 1);
        assertArchivedCount("se_execution_instance_archive", "process_instance_id", processId, 1);
        assertArchivedCount("se_activity_instance_archive", "process_instance_id", processId, 1);
        assertArchivedCount("se_variable_instance_archive", "process_instance_id", processId, 1);
        assertArchivedCount("se_task_assignee_instance_archive", "process_instance_id", processId, 1);
    }

    @Test
    public void testOriginalDataDeleted() {
        long processId = insertCompletedProcess(daysAgo(5));
        insertRelatedData(processId);

        archiveProperties.setRetentionDays(1);
        archiveService.archive("archive_test");

        // Verify original data is deleted
        assertOriginalCount("se_process_instance", "id", processId, 0);
        assertOriginalCount("se_task_instance", "process_instance_id", processId, 0);
        assertOriginalCount("se_execution_instance", "process_instance_id", processId, 0);
        assertOriginalCount("se_activity_instance", "process_instance_id", processId, 0);
        assertOriginalCount("se_variable_instance", "process_instance_id", processId, 0);
        assertOriginalCount("se_task_assignee_instance", "process_instance_id", processId, 0);
    }

    @Test
    public void testAbortedProcessArchived() {
        // Insert an aborted process older than retention
        long processId = insertProcess("aborted", daysAgo(10), daysAgo(5));
        insertRelatedData(processId);

        archiveProperties.setRetentionDays(1);
        int count = archiveService.archive("archive_test");

        Assert.assertEquals(1, count);

        Long archivedCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM se_process_instance_archive WHERE id = ? AND tenant_id = 'archive_test'",
                Long.class, processId);
        Assert.assertEquals(Long.valueOf(1), archivedCount);
    }

    // ========== Helper methods ==========

    private long insertCompletedProcess(Date completeTime) {
        return insertProcess("completed", daysAgo(30), completeTime);
    }

    private long insertProcess(String status, Date createTime, Date completeTime) {
        Timestamp createTs = new Timestamp(createTime.getTime());
        Timestamp completeTs = completeTime != null ? new Timestamp(completeTime.getTime()) : null;

        jdbcTemplate.update(
                "INSERT INTO se_process_instance (gmt_create, gmt_modified, process_definition_id_and_version, " +
                        "status, complete_time, tenant_id) VALUES (?, ?, ?, ?, ?, ?)",
                createTs, createTs, "test:1.0", status, completeTs, "archive_test");

        return jdbcTemplate.queryForObject(
                "SELECT max(id) FROM se_process_instance WHERE tenant_id = 'archive_test'", Long.class);
    }

    private void insertRelatedData(long processId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // activity instance
        jdbcTemplate.update(
                "INSERT INTO se_activity_instance (gmt_create, gmt_modified, process_instance_id, " +
                        "process_definition_id_and_version, process_definition_activity_id, tenant_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                now, now, processId, "test:1.0", "userTask1", "archive_test");

        Long activityId = jdbcTemplate.queryForObject(
                "SELECT max(id) FROM se_activity_instance WHERE tenant_id = 'archive_test' AND process_instance_id = ?",
                Long.class, processId);

        // execution instance
        jdbcTemplate.update(
                "INSERT INTO se_execution_instance (gmt_create, gmt_modified, process_instance_id, " +
                        "process_definition_id_and_version, process_definition_activity_id, activity_instance_id, " +
                        "active, tenant_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                now, now, processId, "test:1.0", "userTask1", activityId, 0, "archive_test");

        Long executionId = jdbcTemplate.queryForObject(
                "SELECT max(id) FROM se_execution_instance WHERE tenant_id = 'archive_test' AND process_instance_id = ?",
                Long.class, processId);

        // task instance
        jdbcTemplate.update(
                "INSERT INTO se_task_instance (gmt_create, gmt_modified, process_instance_id, " +
                        "process_definition_id_and_version, activity_instance_id, process_definition_activity_id, " +
                        "execution_instance_id, status, tenant_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                now, now, processId, "test:1.0", activityId, "userTask1", executionId, "completed", "archive_test");

        Long taskId = jdbcTemplate.queryForObject(
                "SELECT max(id) FROM se_task_instance WHERE tenant_id = 'archive_test' AND process_instance_id = ?",
                Long.class, processId);

        // variable instance
        jdbcTemplate.update(
                "INSERT INTO se_variable_instance (gmt_create, gmt_modified, process_instance_id, " +
                        "execution_instance_id, field_key, field_type, field_string_value, tenant_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                now, now, processId, executionId, "testVar", "string", "testValue", "archive_test");

        // task assignee instance
        jdbcTemplate.update(
                "INSERT INTO se_task_assignee_instance (gmt_create, gmt_modified, process_instance_id, " +
                        "task_instance_id, assignee_id, assignee_type, tenant_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
                now, now, processId, taskId, "user1", "user", "archive_test");
    }

    private Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return cal.getTime();
    }

    private void assertArchivedCount(String table, String column, long id, int expected) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM " + table + " WHERE " + column + " = ? AND tenant_id = 'archive_test'",
                Long.class, id);
        Assert.assertEquals("Expected " + expected + " rows in " + table, Long.valueOf(expected), count);
    }

    private void assertOriginalCount(String table, String column, long id, int expected) {
        Long count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM " + table + " WHERE " + column + " = ? AND tenant_id = 'archive_test'",
                Long.class, id);
        Assert.assertEquals("Expected " + expected + " rows in " + table, Long.valueOf(expected), count);
    }
}
