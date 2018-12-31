package com.alibaba.smart.framework.engine.persister.mongo.entity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssigneePendingTaskEntity extends BaseProcessEntity {

    private String assigneeId;

    private List<String> taskInstanceIdList;




}
