package com.alibaba.smart.framework.engine.persister.mongo.entity;

import java.util.Date;

import lombok.Data;

@Data
public class BaseProcessEntity {
    protected String id;

    protected Date gmtCreate;

    protected Date gmtModified;



}
