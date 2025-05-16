package com.alibaba.smart.framework.engine.service.param.query;

import lombok.Data;



/**
 * 基本查询信息
 *
 * @author yanricheng
 * @date 2025/05/13
 */
@Data
public class BaseQueryParam extends PaginateQueryParam {

    protected String tenantId;

    private Long id;
}
