package com.alibaba.smart.framework.engine.service.param.query;

import java.util.List;

import com.alibaba.smart.framework.engine.query.OrderSpec;

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

    /**
     * Dynamic order by specifications for fluent query API.
     * When set, this will be used instead of default ordering in SQL.
     */
    protected List<OrderSpec> orderBySpecs;

}
