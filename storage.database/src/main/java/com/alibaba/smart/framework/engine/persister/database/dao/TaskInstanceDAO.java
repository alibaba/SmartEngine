package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskInstanceDAO {

    List<TaskInstanceEntity> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);

    Integer countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);





    List<TaskInstanceEntity> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam);

     Integer count(TaskInstanceQueryParam taskInstanceQueryParam);

    List<TaskInstanceEntity> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam);



    //List<TaskInstanceEntity> findList(Long processInstanceId, Long activityInstanceId);

     TaskInstanceEntity findOne(@Param("id") Long id);


     //@Options(useGeneratedKeys = true)
     void insert(  TaskInstanceEntity taskInstanceEntity );

     int update(TaskInstanceEntity taskInstanceEntity);

     void delete(@Param("id") Long id);

}
