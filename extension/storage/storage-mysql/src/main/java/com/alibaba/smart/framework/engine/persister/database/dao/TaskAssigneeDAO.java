package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneeDAO  {

    List<TaskAssigneeEntity> findList(@Param("taskInstanceId")Long  taskInstanceId,@Param("tenantId") String tenantId);

    List<TaskAssigneeEntity> findListForInstanceList(@Param("taskInstanceIdList") List<Long> taskInstanceIdList,@Param("tenantId") String tenantId);

    TaskAssigneeEntity findOne(@Param("id") Long id,@Param("tenantId") String tenantId);


    void insert(  TaskAssigneeEntity taskAssigneeEntity );

    int update(@Param("id") Long id,@Param("assigneeId")String assigneeId,@Param("tenantId") String tenantId);

    void delete(@Param("id") Long id,@Param("tenantId") String tenantId);
}
