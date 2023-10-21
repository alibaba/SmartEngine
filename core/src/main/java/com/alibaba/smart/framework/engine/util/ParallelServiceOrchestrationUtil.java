package com.alibaba.smart.framework.engine.util;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.ParallelGatewayBehavior;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.PvmActivityTask;
import com.alibaba.smart.framework.engine.constant.ParallelGatewayConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public abstract  class ParallelServiceOrchestrationUtil {


    public static Long acquireLatchWaitTime(ExecutionContext context, Map<String, String> properties) {
        Long latchWaitTime = null;

        // 优先从并行网关属性上获取等待超时时间
        String waitTimeout = (String) MapUtil.safeGet(properties, ParallelGatewayConstant.WAIT_TIME_OUT);
        if (StringUtil.isNotEmpty(waitTimeout)) {
            try {
                latchWaitTime = Long.valueOf(waitTimeout);
            } catch (NumberFormatException e) {
                throw new EngineException("latchWaitTime type should be Long");
            }
        }

        // 如果网关属性上未配置超时时间，或格式非法。兜底从request上下文中获取配置
        if (null == latchWaitTime || latchWaitTime <= 0) {
            latchWaitTime = (Long) MapUtil.safeGet(context.getRequest(),
                    RequestMapSpecialKeyConstant.LATCH_WAIT_TIME_IN_MILLISECOND);
        }
        return latchWaitTime;
    }

    public static boolean hasValidLatchWaitTime(Long latchWaitTime) {
        return null != latchWaitTime && latchWaitTime > 0L;
    }

    public static ExecutorService useSpecifiedExecutorServiceIfNeeded(Map<String, String> properties, ProcessEngineConfiguration processEngineConfiguration, ExecutorService executorService) {
        Map<String, ExecutorService> poolsMap = processEngineConfiguration.getExecutorServiceMap();
        String poolName;
        if (poolsMap != null && properties != null && (poolName =
                properties.get(ParallelGatewayConstant.POOL_NAME)) != null
                && poolsMap.containsKey(poolName)) {
            executorService = poolsMap.get(poolName);
        }
        return executorService;
    }

    /**
     * 获取成功的一个future
     * @param futureList future列表
     * @param skipTimeout 是否忽略超时
     * @return 返回第一个成功的future
     */
    public static Future<PvmActivity> getSuccessFuture(List<Future<PvmActivity>> futureList, boolean skipTimeout) {
        if (null == futureList) {
            return null;
        }

        // 没有抑制超时异常，直接获取第一个即可。
        if (!skipTimeout) {
            return futureList.get(0);
        }

        for (Future<PvmActivity> future : futureList) {
            // DONE且是非取消状态
            if (future.isDone() && !future.isCancelled()) {
                return future;
            }
        }
        return null;
    }


    /**
     * 从指定节点开始，进行遍历，找到第一个为并行网关的节点。 注意,暂时不支持并行网关的嵌套，理论可以获取到JoinPvmActivity节点。
     * @param pvmActivity 当前节点
     * @return 并行网关的join节点
     */
    public static PvmActivity findOutTheJoinPvmActivity(PvmActivity pvmActivity ) {


        Map<String, PvmTransition> transitions = pvmActivity.getOutcomeTransitions();

        for (Map.Entry<String, PvmTransition> outcome : transitions.entrySet()) {
            PvmActivity  successorTarget = outcome.getValue().getTarget();

            ActivityBehavior behavior = successorTarget.getBehavior();
            if(behavior instanceof ParallelGatewayBehavior){
                return successorTarget;
            }else{
                PvmActivity   result =   findOutTheJoinPvmActivity(successorTarget);
                if(result != null){
                    return  result;
                }
            }

        }

        throw new EngineException("Unexpected Behavior");

    }

    /**
     * 获取执行策略，默认用ALL兜底
     *
     * @param properties
     * @return
     */
    public static ParallelGatewayConstant.ExecuteStrategy getExecuteStrategy(Map<String, String> properties) {
        if (null == properties || properties.isEmpty()) {
            return ParallelGatewayConstant.ExecuteStrategy.INVOKE_ALL;
        }
        String strategyProp = (String) MapUtil.safeGet(properties, ParallelGatewayConstant.EXE_STRATEGY);
        ParallelGatewayConstant.ExecuteStrategy executeStrategy = null;
        if (StringUtil.isNotEmpty(strategyProp)) {
            executeStrategy = ParallelGatewayConstant.ExecuteStrategy.build(strategyProp);
        }
        if (executeStrategy == null) {
            executeStrategy = ParallelGatewayConstant.ExecuteStrategy.INVOKE_ALL;
        }
        return executeStrategy;
    }

    /**
     * race模式执行，返回最快的一个
     * @param pool 线程池
     * @param tasks 任务集
     * @param timeout 超时时间
     * @param ignoreTimeout 是否忽略超时异常
     * @return future对象
     */
    private static Future<PvmActivity> invokeAnyOf(ExecutorService pool, List<PvmActivityTask> tasks, long timeout,
                                                   boolean ignoreTimeout) throws Exception {

        PvmActivity pvmActivity = null;
        Exception ex = null;

        // 不处理超时的情况
        if (timeout <= 0) {
            pvmActivity = pool.invokeAny(tasks);
        } else {
            // 处理timeout的方式
            try {
                pvmActivity = pool.invokeAny( tasks, timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw e;
            } catch (ExecutionException e) {
                throw e;
            } catch (TimeoutException e) {
                if (!ignoreTimeout) {
                    throw e;
                }
                ex = e;
            }
        }

        return new CompletedFuture<PvmActivity>(pvmActivity, ex);
    }

    public static List<Future<PvmActivity>> invoke(Long latchWaitTime, boolean isSkipTimeout, ParallelGatewayConstant.ExecuteStrategy executeStrategy, ExecutorService executorService, List<PvmActivityTask> pvmActivityTaskList) throws  Exception {

        List<Future<PvmActivity>> futureExecutionResultList = new ArrayList<Future<PvmActivity>>();


        if (hasValidLatchWaitTime(latchWaitTime)) {
            if (executeStrategy.equals(ParallelGatewayConstant.ExecuteStrategy.INVOKE_ALL)) {
                futureExecutionResultList = executorService.invokeAll( pvmActivityTaskList, latchWaitTime, TimeUnit.MILLISECONDS);
            } else {
                Future<PvmActivity> future = invokeAnyOf(executorService, pvmActivityTaskList, latchWaitTime,
                        isSkipTimeout);
                futureExecutionResultList.add(future);
            }
        } else {
            // 超时等待时间为空或不大于0，无需wait
            if (executeStrategy.equals(ParallelGatewayConstant.ExecuteStrategy.INVOKE_ALL)) {
                futureExecutionResultList = executorService.invokeAll( pvmActivityTaskList);
            } else {
                Future<PvmActivity> future = invokeAnyOf(executorService, pvmActivityTaskList, 0,
                        false);
                futureExecutionResultList.add(future);
            }
        }

        return futureExecutionResultList;
    }

    public static boolean isSkipTimeout(String skipTimeoutProp) {
        boolean isSkipTimeout =    Boolean.TRUE.toString().equals(skipTimeoutProp);
        return isSkipTimeout;
    }
}
