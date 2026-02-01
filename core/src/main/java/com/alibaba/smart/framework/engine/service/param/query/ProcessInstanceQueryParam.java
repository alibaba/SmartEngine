package com.alibaba.smart.framework.engine.service.param.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.IdConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessInstanceQueryParam extends BaseQueryParam {

    private String startUserId;
    private String status ;
    private String processDefinitionType;
    private String parentInstanceId;
    private String bizUniqueId;
    private String processDefinitionIdAndVersion;
    /**
     * 流程引擎实例id列表
     */
    private List<String> processInstanceIdList;

    /**
     * 查询启动时间在processStartTime之后的流程实例
     */
    private Date processStartTime;

    /**
     * 查询启动时间在processEndTime之前的流程实例
     */
    private Date processEndTime;

    /**
     * 完成时间开始
     */
    private Date completeTimeStart;

    /**
     * 完成时间结束
     */
    private Date completeTimeEnd;

    // ============ Long type getters for MyBatis (avoid CAST in SQL) ============

    /**
     * Get process instance ID list as Long type.
     * Used by MyBatis to avoid CAST operation in SQL.
     *
     * @return Long type ID list, or null if source is null
     */
    public List<Long> getProcessInstanceIdListAsLong() {
        return IdConverter.toLongList(processInstanceIdList);
    }

    /**
     * Get parent instance ID as Long type.
     * Used by MyBatis to avoid CAST operation in SQL.
     *
     * @return Long type ID, or null if source is null
     */
    public Long getParentInstanceIdAsLong() {
        return IdConverter.toLong(parentInstanceId);
    }
}
