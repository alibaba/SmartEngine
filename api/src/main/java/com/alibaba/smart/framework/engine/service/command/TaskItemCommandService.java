package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

/**
 * 主要负责人工任务处理服务。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskItemCommandService {

    void complete(String taskInstanceId, String subBizId, Map<String, Object> variables);

    void complete(String taskInstanceId, String subBizId, String userId, Map<String, Object> variables);

    //void claim(Long taskId, String userId, Map<String, Object> variables);

}
