package com.alibaba.smart.framework.engine.service.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessInstanceParam extends PaginateRequest {

    private  String startUserId;
    private String  status ;
}
