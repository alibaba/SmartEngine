package com.alibaba.smart.framework.engine.modules.storage.entity;

import com.alibaba.spring.data.mybatis.repository.annotation.Sequence;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExecutionInstanceEntity extends BaseProcessEntity {

    @Id
    @Sequence("execution_instance")
    private Long    id;

    private Long    processInstanceId;

    private String  processDefinitionActivityId;

    private Long    activityInstanceId;

    private boolean active;
}