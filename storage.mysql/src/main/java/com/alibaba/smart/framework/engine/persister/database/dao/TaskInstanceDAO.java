package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

public interface TaskInstanceDAO {

    List<TaskInstanceEntity> findTaskByAssignee(TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam);

    /**
     * 获取指定条件的扩展字段
     * @param taskInstanceQueryByAssigneeParam
     * @return
     */
    List<Map<String,Object>> findTaskCustomFieldsByAssignee(TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam);

    Integer countTaskByAssignee(TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam);

    List<TaskInstanceEntity> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam);

    /**
     * 获取扩展属性，与findTaskList 匹配使用
     * @param taskInstanceQueryParam
     * @return
     */
    List<Map<String,Object>> findTaskCustomFields(TaskInstanceQueryParam taskInstanceQueryParam);



    Integer count(TaskInstanceQueryParam taskInstanceQueryParam);

    List<TaskInstanceEntity> findTaskByProcessInstanceIdAndStatus(Long processInstanceId,String status);



    //List<TaskInstanceEntity> findList(Long processInstanceId, Long activityInstanceId);

    TaskInstanceEntity findOne(@Param("id") Long id);


    //@Options(useGeneratedKeys = true)
    void insert(  TaskInstanceEntity taskInstanceEntity );

    /**
     * 以Map参数的形式创建task
     * @param taskInstanceEntityMap
     */
    void insertWithCustomFields(@Param("taskInstanceEntity")TaskInstanceEntity taskInstanceEntity,@Param("taskInstanceEntityMap") Map<String,Object> taskInstanceEntityMap);

    int update(@Param("taskInstanceEntity") TaskInstanceEntity taskInstanceEntity);

    int updateFromStatus(@Param("taskInstanceEntity") TaskInstanceEntity taskInstanceEntity,@Param("fromStatus") String fromStatus);

    void delete(@Param("id") Long id);

}
