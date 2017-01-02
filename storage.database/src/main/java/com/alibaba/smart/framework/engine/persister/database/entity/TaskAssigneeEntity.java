package com.alibaba.smart.framework.engine.persister.database.entity;

import com.alibaba.spring.data.mybatis.repository.annotation.Sequence;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskAssigneeEntity extends BaseProcessEntity {

    @Id
    @Sequence("task_assignee")
    private Long id;

    private Long processInstanceId;

    private Long executionInstanceId;

    private Long activityInstanceId;

    private Long taskInstanceId;

    private String title;

    private String assigneeId;

    private Integer priority;

    private Date claimTime;

    private Date endTime;
}
