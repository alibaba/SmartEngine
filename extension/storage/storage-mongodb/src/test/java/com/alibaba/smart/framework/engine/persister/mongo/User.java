package com.alibaba.smart.framework.engine.persister.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  20:10.
 */

@Data
@Document
public class User {

    @Id
    private String id;

    private  String name;


}