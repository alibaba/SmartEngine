package com.alibaba.smart.framework.engine.persister.database.entity;

import java.util.Date;

import lombok.Data;

@Data
public class BaseProcessEntity {
    protected Long id;

    protected Date gmtCreate;

    protected Date gmtModified;



}
