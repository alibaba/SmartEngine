package com.alibaba.smart.framework.engine.persister.database.entity;

import com.alibaba.spring.data.mybatis.repository.annotation.Sequence;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VariableInstanceEntity extends BaseProcessEntity {

    @Id
    @Sequence("variable_instance")
    private Long id;

    private String type;

    private Long processInstanceId;

    private Long executionInstanceId;

    private Long taskInstanceId;

    private String assigneeId;

    private Integer priority;

}
