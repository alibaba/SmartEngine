package com.alibaba.smart.framework.engine.service.param.query;

import lombok.Data;

/**
 * Created by yueyu.yr on 2017/9/22.
 *
 * @author yueyu.yr
 * @date 2017/09/22
 */
@Data
public class PaginateQueryParam {

    protected Integer pageOffset;
    protected Integer pageSize;
}
