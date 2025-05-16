package com.alibaba.smart.framework.engine.service.param.command;

import com.alibaba.smart.framework.engine.service.param.query.PaginateQueryParam;
import lombok.Data;


/**
 * 基本命令信息
 *
 * @author yanricheng
 * @date 2025/05/13
 */
@Data
public class BaseCommand {
    protected String tenantId;
}
