package com.alibaba.smart.framework.engine.persister.mongo.entity;

import java.util.Date;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class BaseProcessEntity {
    protected String id;

    protected Date gmtCreate;

    protected Date gmtModified;



}
