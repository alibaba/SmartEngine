package com.alibaba.smart.framework.engine.modules.storage.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseProcessEntity {

    protected Date   gmtCreate;

    protected Date   gmtModified;

    /**
     * 唯一主键,包含了 processDefinitionName和version
     */
    protected String processDefinitionId;

}
