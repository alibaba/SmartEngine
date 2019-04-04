package com.alibaba.smart.framework.engine.service.command;

import java.util.List;
import java.util.Map;

/**
 * 主要负责人工任务处理服务。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskItemCommandService {

    void complete(String taskInstanceId, Map<String, Object> variables);

    void complete(String taskInstanceId, String subBizId, Map<String, Object> variables);

    void complete(String taskInstanceId, String subBizId, String userId, Map<String, Object> variables);

    /**
     *
     * 子任务完成
     * @param taskInstanceId  主任务Id
     * @param subBizIds   子业务id列表
     * @param variables
     *              key:RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID  审批人/认领人
     *              key:RequestMapSpecialKeyConstant.TASK_ITEM_INSTANCE_TAG  子单据据状态
     */
    void complete(String taskInstanceId, List<String> subBizIds, Map<String, Object> variables);

    /**
     * 子任务完成
     * @param taskInstanceId  主任务Id
     * @param subBizIds   子业务id列表
     * @param userId  用户Id
     * @param variables  扩展字段
     *              key:RequestMapSpecialKeyConstant.TASK_ITEM_INSTANCE_TAG  子单据据状态
     */
    void complete(String taskInstanceId, List<String> subBizIds, String userId, Map<String, Object> variables);

}
