package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;

/**
 * 督办管理查询服务接口
 * 
 * @author SmartEngine Team
 */
public interface SupervisionQueryService {

    /**
     * 查询督办记录列表
     * 
     * @param param 查询参数
     * @return 督办记录列表
     */
    List<SupervisionInstance> findSupervisionList(SupervisionQueryParam param);

    /**
     * 统计督办记录数量
     * 
     * @param param 查询参数
     * @return 督办记录数量
     */
    Long countSupervision(SupervisionQueryParam param);

    /**
     * 查询任务的活跃督办记录
     * 
     * @param taskInstanceId 任务实例ID
     * @param tenantId 租户ID
     * @return 活跃督办记录列表
     */
    List<SupervisionInstance> findActiveSupervisionByTask(String taskInstanceId, String tenantId);

    /**
     * 根据ID查询督办记录
     * 
     * @param supervisionId 督办ID
     * @param tenantId 租户ID
     * @return 督办记录
     */
    SupervisionInstance findOne(String supervisionId, String tenantId);
}