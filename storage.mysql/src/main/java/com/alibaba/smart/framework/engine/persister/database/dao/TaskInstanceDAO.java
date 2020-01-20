package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import org.apache.ibatis.annotations.Param;

public interface TaskInstanceDAO {

    List<TaskInstanceEntity> findTaskByAssignee(TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam);

    Integer countTaskByAssignee(TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam);

    List<TaskInstanceEntity> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam);

    Integer count(TaskInstanceQueryParam taskInstanceQueryParam);

    List<TaskInstanceEntity> findTaskByProcessInstanceIdAndStatus(Long processInstanceId,String status);


    TaskInstanceEntity findOne(@Param("id") Long id);


    void insert(  TaskInstanceEntity taskInstanceEntity );

    int update(@Param("taskInstanceEntity") TaskInstanceEntity taskInstanceEntity);

    int updateFromStatus(@Param("taskInstanceEntity") TaskInstanceEntity taskInstanceEntity,@Param("fromStatus") String fromStatus);

    void delete(@Param("id") Long id);

}
