package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneeDAO  {


    List<TaskAssigneeEntity> findList(Long  taskInstanceId);

    List<TaskAssigneeEntity> findListForInstanceList(List<Long> taskInstanceIdList);

    TaskAssigneeEntity findOne(@Param("id") Long id);


    //@Options(useGeneratedKeys = true)
    void insert(  TaskAssigneeEntity taskAssigneeEntity );

    int update(@Param("id") Long id,@Param("assigneeId")Long assigneeId);

    void delete(@Param("id") Long id);
}
