package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.persister.database.entity.SupervisionInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;

import org.apache.ibatis.annotations.Param;

/**
 * 督办记录DAO接口
 * 
 * @author SmartEngine Team
 */
public interface SupervisionInstanceDAO {

    /**
     * 插入督办记录
     */
    void insert(SupervisionInstanceEntity supervisionInstanceEntity);

    /**
     * 更新督办记录
     */
    int update(@Param("supervisionInstanceEntity") SupervisionInstanceEntity supervisionInstanceEntity);

    /**
     * 根据ID查询督办记录
     */
    SupervisionInstanceEntity findOne(@Param("id") Long id, @Param("tenantId") String tenantId);

    /**
     * 根据任务ID查询活跃的督办记录
     */
    List<SupervisionInstanceEntity> findActiveByTaskId(@Param("taskInstanceId") Long taskInstanceId, 
                                                      @Param("tenantId") String tenantId);

    /**
     * 根据督办人查询督办记录
     */
    List<SupervisionInstanceEntity> findBySupervisor(@Param("supervisorUserId") String supervisorUserId,
                                                    @Param("tenantId") String tenantId,
                                                    @Param("pageOffset") Integer pageOffset,
                                                    @Param("pageSize") Integer pageSize);

    /**
     * 统计督办记录数量
     */
    Integer countBySupervisor(@Param("supervisorUserId") String supervisorUserId,
                             @Param("tenantId") String tenantId);

    /**
     * 关闭督办记录
     */
    int closeSupervision(@Param("id") Long id, @Param("tenantId") String tenantId);

    /**
     * 根据任务ID批量关闭督办记录
     */
    int closeByTaskId(@Param("taskInstanceId") Long taskInstanceId, @Param("tenantId") String tenantId);

    /**
     * 删除督办记录
     */
    void delete(@Param("id") Long id, @Param("tenantId") String tenantId);

    /**
     * 根据查询参数综合查询督办记录
     */
    List<SupervisionInstanceEntity> findByQuery(SupervisionQueryParam param);

    /**
     * 根据查询参数统计督办记录数量
     */
    Integer countByQuery(SupervisionQueryParam param);
}