package com.alibaba.smart.framework.engine.modules.storage.entity;

import org.springframework.data.annotation.Id;

import com.alibaba.spring.data.mybatis.repository.annotation.Sequence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class ProcessInstanceEntity extends BaseProcessEntity {

    @Id
    @Sequence("process_instance")
    private Long id;
    
    private Long   parentProcessInstanceId;

    private String status;
    
}
