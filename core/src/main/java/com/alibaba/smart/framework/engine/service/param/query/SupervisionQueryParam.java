package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.IdConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Supervision query parameter.
 *
 * @author SmartEngine Team
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SupervisionQueryParam extends BaseQueryParam {

    /**
     * Supervisor user ID
     */
    private String supervisorUserId;

    /**
     * Task instance ID list (String type for API compatibility)
     */
    private List<String> taskInstanceIdList;

    /**
     * Process instance ID list (String type for API compatibility)
     */
    private List<String> processInstanceIdList;

    /**
     * Supervision type
     */
    private String supervisionType;

    /**
     * Supervision status
     */
    private String status;

    /**
     * Supervision start time
     */
    private Date supervisionStartTime;

    /**
     * Supervision end time
     */
    private Date supervisionEndTime;

    // ============ Long type getters for MyBatis (avoid CAST in SQL) ============

    /**
     * Get task instance ID list as Long type.
     * Used by MyBatis to avoid CAST operation in SQL.
     *
     * @return Long type ID list, or null if source is null
     */
    public List<Long> getTaskInstanceIdListAsLong() {
        return IdConverter.toLongList(taskInstanceIdList);
    }

    /**
     * Get process instance ID list as Long type.
     * Used by MyBatis to avoid CAST operation in SQL.
     *
     * @return Long type ID list, or null if source is null
     */
    public List<Long> getProcessInstanceIdListAsLong() {
        return IdConverter.toLongList(processInstanceIdList);
    }
}
