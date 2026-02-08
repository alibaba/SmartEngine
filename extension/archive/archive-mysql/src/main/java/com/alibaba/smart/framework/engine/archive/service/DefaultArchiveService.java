package com.alibaba.smart.framework.engine.archive.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.archive.config.ArchiveProperties;
import com.alibaba.smart.framework.engine.archive.dao.ArchiveDAO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
public class DefaultArchiveService implements ArchiveService {

    @Setter
    private ArchiveDAO archiveDAO;

    @Setter
    private ArchiveProperties archiveProperties;

    @Setter
    private TransactionTemplate transactionTemplate;

    @Override
    public int archive(String tenantId) {
        if (!archiveProperties.isEnabled()) {
            log.debug("Archive is disabled, skipping");
            return 0;
        }

        Date completedBefore = calculateCutoffDate();
        int totalArchived = 0;

        while (true) {
            List<Long> processIds = archiveDAO.findArchivableProcessIds(
                    completedBefore, tenantId, archiveProperties.getBatchSize());

            if (processIds == null || processIds.isEmpty()) {
                break;
            }

            int batchCount = archiveBatch(processIds, tenantId);
            totalArchived += batchCount;

            log.info("Archived batch of {} process instances, total so far: {}", batchCount, totalArchived);
        }

        if (totalArchived > 0) {
            log.info("Archive completed. Total archived process instances: {}", totalArchived);
        }

        return totalArchived;
    }

    private int archiveBatch(List<Long> processIds, String tenantId) {
        Integer result = transactionTemplate.execute(status -> {
            try {
                // Step 1: INSERT...SELECT archive all 11 tables
                archiveDAO.archiveProcessInstances(processIds, tenantId);
                archiveDAO.archiveTaskInstances(processIds, tenantId);
                archiveDAO.archiveExecutionInstances(processIds, tenantId);
                archiveDAO.archiveActivityInstances(processIds, tenantId);
                archiveDAO.archiveVariableInstances(processIds, tenantId);
                archiveDAO.archiveTaskAssigneeInstances(processIds, tenantId);
                archiveDAO.archiveSupervisionInstances(processIds, tenantId);
                archiveDAO.archiveNotificationInstances(processIds, tenantId);
                archiveDAO.archiveTaskTransferRecords(processIds, tenantId);
                archiveDAO.archiveAssigneeOperationRecords(processIds, tenantId);
                archiveDAO.archiveProcessRollbackRecords(processIds, tenantId);

                // Step 2: DELETE from original tables (child tables first, main table last)
                archiveDAO.deleteTaskTransferRecords(processIds, tenantId);
                archiveDAO.deleteAssigneeOperationRecords(processIds, tenantId);
                archiveDAO.deleteSupervisionInstances(processIds, tenantId);
                archiveDAO.deleteNotificationInstances(processIds, tenantId);
                archiveDAO.deleteProcessRollbackRecords(processIds, tenantId);
                archiveDAO.deleteTaskAssigneeInstances(processIds, tenantId);
                archiveDAO.deleteVariableInstances(processIds, tenantId);
                archiveDAO.deleteTaskInstances(processIds, tenantId);
                archiveDAO.deleteExecutionInstances(processIds, tenantId);
                archiveDAO.deleteActivityInstances(processIds, tenantId);
                archiveDAO.deleteProcessInstances(processIds, tenantId);

                return processIds.size();
            } catch (Exception e) {
                log.error("Failed to archive batch, rolling back. processIds: {}", processIds, e);
                status.setRollbackOnly();
                throw e;
            }
        });

        return result != null ? result : 0;
    }

    private Date calculateCutoffDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -archiveProperties.getRetentionDays());
        return cal.getTime();
    }
}
