package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.GatewaySticker;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.ParallelServiceOrchestration;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.ParallelGatewayConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by 高海军 帝奇 74394 on  2020-09-21 17:59.
 */
@Slf4j
public class DefaultParallelServiceOrchestration implements ParallelServiceOrchestration {
    @Override
    public void orchestrateService(ExecutionContext context, PvmActivity pvmActivity) {{

        Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();
        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();

        int outComeTransitionSize = outcomeTransitions.size();
        int inComeTransitionSize = incomeTransitions.size();


        if (outComeTransitionSize >= 2 && inComeTransitionSize == 1) {
            //并发执行fork
            ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
            ExecutorService executorService = processEngineConfiguration.getExecutorService();

            List<PvmActivityTask> tasks = new ArrayList<PvmActivityTask>(outComeTransitionSize);

            AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

            ContextFactory contextFactory = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
                ContextFactory.class);

            for (Entry<String, PvmTransition> pvmTransitionEntry : outcomeTransitions.entrySet()) {
                PvmActivity target = pvmTransitionEntry.getValue().getTarget();

                //从ParentContext 复制父Context到子线程内。这里得注意下线程安全。
                ExecutionContext subThreadContext = contextFactory.createChildThreadContext(context);

                PvmActivityTask task = new PvmActivityTask(target, subThreadContext);

                tasks.add(task);
            }


            try {

                Long latchWaitTime = null;

                // 优先从并行网关属性上获取等待超时时间
                String waitTimeout = (String) MapUtil.safeGet(pvmActivity.getModel().getProperties(),
                        ParallelGatewayConstant.WAIT_TIME_OUT);
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

                List<Future<PvmActivity>> futureExecutionResultList ;
                if(null != latchWaitTime && latchWaitTime > 0L){
                    futureExecutionResultList = executorService.invokeAll(tasks,latchWaitTime, TimeUnit.MILLISECONDS);
                }else {
                    // 超时等待时间为空或不大于0，无需wait
                    futureExecutionResultList = executorService.invokeAll(tasks);
                }

                //注意这里的逻辑：这里假设是子线程在执行某个fork分支的逻辑后，然后会在join节点时返回。这个join节点就是 futureJoinParallelGateWay。
                // 当await 执行结束后，这里的假设不变式：所有子线程都已经到达了join节点。
                ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();

                for (Future<PvmActivity> pvmActivityFuture : futureExecutionResultList) {
                    try{
                        pvmActivityFuture.get();
                    }catch (InterruptedException e){
                        exceptionProcessor.process(e,context);
                    }catch (ExecutionException e){
                        exceptionProcessor.process(e,context);
                    }
                }


                Future<PvmActivity> pvmActivityFuture = futureExecutionResultList.get(0);
                PvmActivity futureJoinParallelGateWay = pvmActivityFuture.get();
                ActivityBehavior behavior = futureJoinParallelGateWay.getBehavior();

                //模拟正常流程的继续驱动，将继续推进caller thread 执行后续节点。
                behavior.leave(context,futureJoinParallelGateWay);

            } catch (Exception e) {
                throw new EngineException(e);
            }


        } else if (outComeTransitionSize == 1 && inComeTransitionSize >= 2) {

            //在服务编排场景，仅是子线程在执行到最后一个节点后，会进入到并行网关的join节点。CallerThread 不会执行到这里的逻辑。
            GatewaySticker.create().setPvmActivity(pvmActivity);

        }else{
            throw new EngineException("Should not touch here:"+pvmActivity);
        }
    }

    }
}