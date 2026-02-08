package com.alibaba.smart.framework.engine.archive.config;

import lombok.Data;

@Data
public class ArchiveProperties {

    /**
     * Whether archive is enabled. Default is false.
     */
    private boolean enabled = false;

    /**
     * Number of days to retain completed/aborted process instances before archiving.
     */
    private int retentionDays = 90;

    /**
     * Number of process instances to archive per batch.
     */
    private int batchSize = 100;

    /**
     * Cron expression for the archive scheduler.
     */
    private String cron = "0 0 2 * * ?";
}
