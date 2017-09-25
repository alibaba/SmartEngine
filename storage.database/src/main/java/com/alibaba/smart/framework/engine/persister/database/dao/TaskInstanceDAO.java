package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface TaskInstanceDAO {

     List<TaskInstanceEntity> findPendingTask(TaskInstanceQueryParam taskInstanceQueryParam);

     List<TaskInstanceEntity> findTask(Long processInstanceId, Long activityInstanceId);

     TaskInstanceEntity findOne(@Param("id") Long id);


     //@Options(useGeneratedKeys = true)
     void insert(  TaskInstanceEntity taskInstanceEntity );

     int update(TaskInstanceEntity taskInstanceEntity);

     void delete(@Param("id") Long id);

}
