package com.alibaba.smart.framework.engine.service.param.query;

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

    private  String startUserId;
    private String  status ;
    private String processDefinitionType;

}
