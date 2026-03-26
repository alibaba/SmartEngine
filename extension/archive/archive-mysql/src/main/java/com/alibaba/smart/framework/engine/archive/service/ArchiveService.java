package com.alibaba.smart.framework.engine.archive.service;

public interface ArchiveService {

    /**
     * Archive completed/aborted process instances for the given tenant.
     *
     * @param tenantId tenant id, nullable for single-tenant mode
     * @return total number of archived process instances
     */
    int archive(String tenantId);
}
