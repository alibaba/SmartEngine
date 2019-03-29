package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.TaskItemInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskItemInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;

import org.apache.ibatis.annotations.Param;

public interface TaskItemInstanceDAO {

    List<TaskItemInstanceEntity> findTaskItemList(TaskItemInstanceQueryParam TaskItemInstanceQueryParam);

    List<TaskItemInstanceEntity> findList(@Param("idList") List<Long> idList);

    Integer count(TaskItemInstanceQueryParam TaskItemInstanceQueryParam);

    TaskItemInstanceEntity findOne(@Param("id") Long id);

    TaskItemInstanceEntity find(@Param("taskInstanceId") String taskInstanceId, @Param("subBizId") String subBizId);

    //@Options(useGeneratedKeys = true)
    void insert(TaskItemInstanceEntity taskItemInstanceEntity);

    void batchInsert(@Param("taskItemInstanceEntityList") List<TaskItemInstanceEntity> taskItemInstanceEntityList);

    int update(@Param("taskItemInstanceEntity") TaskItemInstanceEntity taskItemInstanceEntity);

    int updateFromStatus(@Param("taskItemInstanceEntity") TaskItemInstanceEntity taskItemInstanceEntity,
                         @Param("fromStatus") String fromStatus);

    /**
     * 批量更新状态
     * @param taskItemInstanceEntity
     * @return
     */
    int updateStatusBatch(@Param("taskItemInstanceEntity") TaskItemInstanceEntity taskItemInstanceEntity,
                          @Param("taskItemIdList") List<Long> taskItemIdList, @Param("fromStatus") String fromStatus);

    void delete(@Param("id") Long id);

}
