package com.alibaba.smart.framework.engine.service.param.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
     * Task instance ID list
     */
    private List<? extends Serializable> taskInstanceIdList;

    /**
     * Process instance ID list
     */
    private List<? extends Serializable> processInstanceIdList;

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

}
