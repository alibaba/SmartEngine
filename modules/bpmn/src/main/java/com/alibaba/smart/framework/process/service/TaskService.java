package com.alibaba.smart.framework.process.service;

import com.alibaba.smart.framework.process.model.runtime.command.impl.TaskInstanceCommonActionCommand;
import com.alibaba.smart.framework.process.model.runtime.command.impl.TaskInstanceCreateCommand;

public interface TaskService {

    void create(TaskInstanceCreateCommand<?> command);

    void claim(TaskInstanceCommonActionCommand<?> command);

    void unClaim(TaskInstanceCommonActionCommand<?> command);

    void complete(TaskInstanceCommonActionCommand<?> command);

    /**
     * 将任务委托给别人处理,与{@link #reAssigne(TaskInstanceCommonActionCommand)} 语义不一样,作用相同.
     * 
     * @param command
     */
    void delegate(TaskInstanceCommonActionCommand<?> command);

    /**
     * 更新任务的处理者
     * 
     * @param command
     */
    void reAssigne(TaskInstanceCommonActionCommand<?> command);

    /**
     * 一个任务可以有多个候选处理者,任意一个处理完成后即可.
     * 
     * @param taskId
     * @param userId
     * @param identityLinkType
     */
    void addUserIdentityLink(String taskId, String userId, String identityLinkType);

}
