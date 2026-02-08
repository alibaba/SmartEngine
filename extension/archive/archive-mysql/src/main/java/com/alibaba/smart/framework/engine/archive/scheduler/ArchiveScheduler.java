package com.alibaba.smart.framework.engine.archive.scheduler;

import java.util.concurrent.ScheduledFuture;

import com.alibaba.smart.framework.engine.archive.config.ArchiveProperties;
import com.alibaba.smart.framework.engine.archive.service.ArchiveService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Slf4j
public class ArchiveScheduler {

    @Setter
    private ArchiveProperties archiveProperties;

    @Setter
    private ArchiveService archiveService;

    @Setter
    private TaskScheduler taskScheduler;

    @Setter
    private String tenantId;

    private ScheduledFuture<?> scheduledFuture;

    public void start() {
        if (!archiveProperties.isEnabled()) {
            log.info("Archive scheduler is disabled");
            return;
        }

        CronTrigger trigger = new CronTrigger(archiveProperties.getCron());
        scheduledFuture = taskScheduler.schedule(this::executeArchive, trigger);
        log.info("Archive scheduler started with cron: {}", archiveProperties.getCron());
    }

    public void stop() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            log.info("Archive scheduler stopped");
        }
    }

    private void executeArchive() {
        try {
            log.info("Archive job started");
            int count = archiveService.archive(tenantId);
            log.info("Archive job completed, archived {} process instances", count);
        } catch (Exception e) {
            log.error("Archive job failed", e);
        }
    }
}
