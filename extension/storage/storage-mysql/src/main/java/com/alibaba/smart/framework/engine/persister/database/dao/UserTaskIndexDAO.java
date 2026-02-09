package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.UserTaskIndexEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;

import org.apache.ibatis.annotations.Param;

public interface UserTaskIndexDAO {

    void insert(UserTaskIndexEntity entity);

    void deleteByTaskInstanceId(@Param("taskInstanceId") Long taskInstanceId, @Param("tenantId") String tenantId);

    void deleteByAssigneeAndTask(@Param("assigneeId") String assigneeId, @Param("taskInstanceId") Long taskInstanceId, @Param("tenantId") String tenantId);

    void updateTaskStatus(@Param("taskInstanceId") Long taskInstanceId, @Param("taskStatus") String taskStatus, @Param("tenantId") String tenantId);

    void updateTaskFields(@Param("taskInstanceId") Long taskInstanceId, @Param("title") String title, @Param("priority") Integer priority, @Param("domainCode") String domainCode, @Param("tenantId") String tenantId);

    List<UserTaskIndexEntity> findByAssignee(TaskInstanceQueryByAssigneeParam param);

    Integer countByAssignee(TaskInstanceQueryByAssigneeParam param);
}
