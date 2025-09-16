package com.alibaba.smart.framework.engine.service.param.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

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
    private String status;
    private String processDefinitionType;
    private String parentInstanceId;
    private String bizUniqueId;
    private String processDefinitionIdAndVersion;

    /** 流程引擎实例id列表 */
    private List<String> processInstanceIdList;

    /** 查询启动时间在processStartTime之后的流程实例 */
    private Date processStartTime;

    /** 查询启动时间在processEndTime之前的流程实例 */
    private Date processEndTime;
}
