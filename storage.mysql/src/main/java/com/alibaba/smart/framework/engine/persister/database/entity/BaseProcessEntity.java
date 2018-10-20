package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseProcessEntity {
    protected Long id;

    protected Date gmtCreate;

    protected Date gmtModified;



}
