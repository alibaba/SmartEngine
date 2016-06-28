package com.alibaba.smart.framework.engine.modules.storage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.data.annotation.Id;

import com.alibaba.spring.data.mybatis.repository.annotation.Sequence;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ActivityInstanceEntity extends BaseProcessEntity {

    @Id
    @Sequence("activity_instance")
    private Long   id;

    private Long   processInstanceId;

    private String processDefinitionActivityId;

    private Long   executionInstanceId;

    private Long   taskInstanceId;
}
