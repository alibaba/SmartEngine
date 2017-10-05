package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseProcessEntity {

    protected Date gmtCreate;

    protected Date gmtModified;

    /**
     * 唯一主键,包含了 processDefinitionId和version
     */
    protected String processDefinitionIdAndVersion;

}
