package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskInstanceCommandService {

    void complete(Long taskId,Map<String, Object> variables);

    void complete(Long taskId,Long userId, Map<String, Object> variables);

    void claim(Long taskId,Long userId, Map<String, Object> variables);

}
