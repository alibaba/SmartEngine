package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

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
public class ProcessInstanceQueryParam extends PaginateQueryParam {

    private String startUserId;
    private String status ;
    private String processDefinitionType;
    private String bizUniqueId;
    private String processDefinitionIdAndVersion;
    /**
     * 流程引擎实例id列表
     */
    private List<Long> processInstanceIdList;
}
