package com.alibaba.smart.framework.engine.modules.storage.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.data.annotation.Id;

import com.alibaba.spring.data.mybatis.repository.annotation.Sequence;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskInstanceEntity extends BaseProcessEntity {

    @Id
    @Sequence("task_instance")
    private Long    id;

    private Long    processInstanceId;

    private Long    executionInstanceId;

    private Long    activityInstanceId;

    private String  assigneeId;

    private Integer priority;

    private Date    claimTime;

    private Date    endTime;
}
