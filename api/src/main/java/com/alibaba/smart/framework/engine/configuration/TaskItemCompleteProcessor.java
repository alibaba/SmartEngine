package com.alibaba.smart.framework.engine.configuration;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.Activity;

/**
 * TaskItemCompleteProcessor
 * <pre>
 * 子任务审批扩展点
 *  </pre>
 *
 * @author xiuqun.hxq@alibaba-inc.com
 * @author zeyuan.zy@alibaba-inc.com
 */
public interface TaskItemCompleteProcessor {

    /**
     * 前置处理
     * @param processInstanceId  流程实例id
     * @param activityInstanceId 节点实例id
     * @param taskInstanceId   主任务实例id
     * @param taskItemInstanceId  子任务实例id
     * @param variables   上下文参数
     * @param activity    活动节点
     * @param smartEngine 引擎入口
     */
    void postProcessBeforeTaskItemComplete(String processInstanceId, String activityInstanceId, String taskInstanceId,
                                           List<String> taskItemInstanceId, Map<String, Object> variables, Activity activity, SmartEngine smartEngine);

    /**
     * 后置处理
     * @param processInstanceId  流程实例id
     * @param activityInstanceId 节点实例id
     * @param taskInstanceId   主任务实例id
     * @param taskItemInstanceId  子任务实例id
     * @param variables   上下文参数
     * @param activity    活动节点
     * @param smartEngine 引擎入口
     */
    void postProcessAfterTaskItemComplete(String processInstanceId, String activityInstanceId, String taskInstanceId,
                                          List<String> taskItemInstanceId, Map<String, Object> variables, Activity activity, SmartEngine smartEngine);

    /**
     * 核心处理
     * 是否可以关闭当前主任务
     * 返回值： key=canComplete(是否可以关闭当前主任务)
     * key=tag(如果可以驱动到下一个节点，则当前节点的审核结果的标签值)
     * 例子：Map<String,String> map = new HashMap<>;
     *      map.put("canComplete","true");
     *      map.put("tag","pass");
     * @param processInstanceId  流程实例id
     * @param taskInstanceId   任务实例id
     * @param activity      节点
     * @param smartEngine   引擎入口
     */
    Map<String,String> canCompleteCurrentMainTask(String processInstanceId, String taskInstanceId, Activity activity, SmartEngine smartEngine);

    /**
     * 根据活动实例id,获取审批通过的子业务ID
     * @param activityInstanceId 活动实例id
     * @return
     */
    List<String> getPassedSubBizIdByActivityInstanceId(String activityInstanceId, SmartEngine smartEngine);
}
