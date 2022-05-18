package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.GatewaySticker;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.ParallelServiceOrchestration;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.ParallelGatewayConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

import static com.alibaba.smart.framework.engine.util.ParallelServiceOrchestrationUtil.*;

/**
 * Created by 高海军 帝奇 74394 on  2020-09-21 17:59.
 */
@Slf4j
public class DefaultParallelServiceOrchestration implements ParallelServiceOrchestration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultParallelServiceOrchestration.class);

    @Override
    public void orchestrateService(ExecutionContext context, PvmActivity pvmActivity) {
        {

            Map<String, PvmTransition> incomeTransitionMap = pvmActivity.getIncomeTransitions();
            Map<String, PvmTransition> outcomeTransitionMap = pvmActivity.getOutcomeTransitions();

            int outComeTransitionSize = outcomeTransitionMap.size();
            int inComeTransitionSize = incomeTransitionMap.size();

            //fork
            if (outComeTransitionSize >= 2 && inComeTransitionSize == 1) {
                ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
                AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
                ContextFactory contextFactory = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, ContextFactory.class);
                Map<String, String> properties = pvmActivity.getModel().getProperties();
                Set<Entry<String, PvmTransition>> entries = outcomeTransitionMap.entrySet();

                Long latchWaitTime = acquireLatchWaitTime(context, properties);
                ParallelGatewayConstant.ExecuteStrategy executeStrategy = getExecuteStrategy(properties);
                boolean isSkipTimeout = isSkipTimeout((String) MapUtil.safeGet(properties, ParallelGatewayConstant.SKIP_TIMEOUT_EXCEPTION));

                // 默认线程池
                ExecutorService executorService = processEngineConfiguration.getExecutorService();
                // 注意: 重新赋值 如果能匹配到自定义的线程池，直接使用。 允许扩展并行网关的3种属性: timeout="300" strategy="any" poolName="poolA" skipTimeoutExp="true"  使用方法详见  ServiceOrchestrationParallelGatewayTest
                executorService = useSpecifiedExecutorServiceIfNeeded(properties, processEngineConfiguration, executorService);

                List<PvmActivityTask> pvmActivityTaskList = new ArrayList<PvmActivityTask>(outComeTransitionSize);

                try {

                    PvmActivity finalJoinPvmActivity = initMultiTaskRequestAndFindOutJoinActivity(context, contextFactory, pvmActivityTaskList, entries);

                    List<Future<PvmActivity>> futureExecutionResultList = invoke(latchWaitTime, isSkipTimeout, executeStrategy, executorService, pvmActivityTaskList);

                    accquireFutureResult(context, processEngineConfiguration, latchWaitTime, isSkipTimeout, futureExecutionResultList);

//                // 获取第一个成功执行的future
//                Future<PvmActivity> pvmActivityFuture = getSuccessFuture(futureExecutionResultList, isSkipTimeoutExp);
//
//                PvmActivity futureJoinParallelGateWayPvmActivity = null;
//                if(null == pvmActivityFuture) {
//                    // 如果没有找到，只有一种可能就是子任务全超时被cancel了。直接使用finalJoinActivity
//                    futureJoinParallelGateWayPvmActivity = firstJoinParallelGateWayPvmActivity;
//                } else {
//                    // 直接从future中获取join事件节点
//                    futureJoinParallelGateWayPvmActivity = pvmActivityFuture.get();
//                }

//                PvmActivity futureJoinParallelGateWayPvmActivity = finalJoinParallelGateWayPvmActivity;

                    ActivityBehavior behavior = finalJoinPvmActivity.getBehavior();

                    //模拟正常流程的继续驱动，将继续推进caller thread 执行后续节点。
                    behavior.leave(context, finalJoinPvmActivity);

                } catch (Exception e) {
                    throw new EngineException(e);
                }


            } else if (outComeTransitionSize == 1 && inComeTransitionSize >= 2) {
                //join
                // 在服务编排场景，仅是子线程在执行到最后一个节点后，会进入到并行网关的join节点。CallerThread 不会执行到这里的逻辑。
                // TUNE 目前重构下来,感觉session 里去setPvmActivity,没啥意义了. 直接根据流程定义中,计算出join节点即可.
                GatewaySticker.currentSession().setPvmActivity(pvmActivity);

            } else {
                throw new EngineException("Should not touch here:" + pvmActivity);
            }
        }

    }

    /**
     *  这个为protected ,意图是: 有些分布式场景,需要全链路打印日志,这里建议有这方面需求的自定义实现 PvmActivityTask ,传入相关的Context 进来即可.
     */
    protected PvmActivity initMultiTaskRequestAndFindOutJoinActivity(ExecutionContext context, ContextFactory contextFactory, List<PvmActivityTask> pvmActivityTaskList, Set<Entry<String, PvmTransition>> entries) {

        PvmActivity finalJoinParallelGateWayPvmActivity = null;
        for (Entry<String, PvmTransition> pvmTransitionEntry : entries) {

            //target 为fork 节点的后继节点，比如service1，service3
            PvmActivity target = pvmTransitionEntry.getValue().getTarget();

            //TUNE 重复计算了,但是带来的好处就是检查fork后面的每个sequenceFlow最后指向的都是同一个Join节点.
            PvmActivity current = findOutTheJoinPvmActivity(target);

            if (finalJoinParallelGateWayPvmActivity != null && !finalJoinParallelGateWayPvmActivity.equals(current)) {
                throw new EngineException("not support embedded fork join");
            }

            finalJoinParallelGateWayPvmActivity = current;

            //将ParentContext 复制到 子线程内
            ExecutionContext subThreadContext = contextFactory.createChildThreadContext(context);

            PvmActivityTask pvmActivityTask = new PvmActivityTask(target, subThreadContext);

            LOGGER.debug("PvmActivityTask thread id  is {}, subThreadContext is {} {} ", Thread.currentThread().getId(), subThreadContext);

            pvmActivityTaskList.add(pvmActivityTask);
        }

        if (null == finalJoinParallelGateWayPvmActivity) {
            throw new EngineException("finalJoinParallelGateWayPvmActivity can not be empty");
        }

        return finalJoinParallelGateWayPvmActivity;
    }

    private void accquireFutureResult(ExecutionContext context, ProcessEngineConfiguration processEngineConfiguration, Long latchWaitTime, boolean isSkipTimeoutExp, List<Future<PvmActivity>> futureExecutionResultList) throws TimeoutException {
        //注意这里的逻辑：这里假设是子线程在执行某个fork分支的逻辑后，然后会在join节点时返回。这个join节点就是 futureJoinParallelGateWay。
        // 当await 执行结束后，这里的假设不变式：所有子线程都已经到达了join节点。
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

        for (Future<PvmActivity> pvmActivityFuture : futureExecutionResultList) {
            try {
                if (hasValidLatchWaitTime(latchWaitTime)) {
                    pvmActivityFuture.get(latchWaitTime, TimeUnit.MILLISECONDS);
                } else {
                    pvmActivityFuture.get();
                }
            } catch (InterruptedException e) {
                exceptionProcessor.process(e, context);
            } catch (ExecutionException e) {
                exceptionProcessor.process(e, context);
            } catch (CancellationException e) {
                // 忽略超时异常
                if (isSkipTimeoutExp) {
                    // 跳过超时异常，只记录log
                    log.warn("parallel gateway occur timeout, skip exception!", e);
                } else {
                    throw e;
                }
            }
        }
    }

}