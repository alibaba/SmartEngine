package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.tree.ActivityTreeNode;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.InclusiveGatewayHelper.*;

/**
 * 代码逻辑整体说明:
 *   从功能上讲,包容网关类似于并行网关,只不过叠加了类似互斥网关的特性,是否触发对应的分支,是由对应的Transition上的表达式决定的.
 *   另外,看到了有些开源引擎支持了 unbalanced inclusiveGateway特性的, 所以我也决定支持下.
 *
 *   从实现逻辑上来讲,有几个难点:
 *   1. 包容网关的 fork 和 join 节点都是xml 里面的 inclusiveGateway 节点,所以天然需要区分,但是职责上还是违反了 SRP 的.
 *   2. 因为触发对应的分支是由表达式条件决定的,所以必须在 fork 网关 leave 时,记录下 triggered activityIds
 *   3. 由于存在unbalanced 和 embedded 这两种情况存在 ,所以不能简单的在 join 环节确定对应的 fork 的triggered activityIds.
 *   4. 由于通常在子线程运行 fork 逻辑,而子线程通常无法简单获得主线程未提交的数据,所以需要额外处理
 */
@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = InclusiveGateway.class)
public class InclusiveGatewayBehavior extends AbstractActivityBehavior<InclusiveGateway> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InclusiveGatewayBehavior.class);


    public InclusiveGatewayBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        // InclusiveGateway only works in DataBase model

        InclusiveGateway inclusiveGateway = (InclusiveGateway)pvmActivity.getModel();

        return innerEnter(context, pvmActivity, inclusiveGateway);

    }

    protected void hookEnter(ExecutionContext context, PvmActivity pvmActivity) {

        if(CommonGatewayHelper.isJoinGateway(pvmActivity)){
            return;
        }

        // 重要,在 fork 环节,设置了context#BlockId, 这样后续所有 EI 都会打上这个标记. 便于后续在 join 环节找到对应的 fork (主要服务于 unbalanced gateway)
        String instanceId = context.getExecutionInstance().getInstanceId();
        context.setBlockId(instanceId);

    }


    private boolean innerEnter(ExecutionContext context, PvmActivity pvmActivity, InclusiveGateway inclusiveGateway) {

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            //先简单处理 ,然后重点是在 leave 阶段，根据触发条件表达式和相关配置, 决定是否并发创建 pvmActivity
            super.enter(context, pvmActivity);

            return false;


        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            return handleJoinGatewayEnter(context, pvmActivity, inclusiveGateway, variablePersister);

        }else{
            throw new EngineException("Unexpected behavior: "+pvmActivity);
        }

    }

    private boolean handleJoinGatewayEnter(ExecutionContext context, PvmActivity joinedPvmActivity, InclusiveGateway gateway, VariablePersister variablePersister) {
        ProcessInstance processInstance = context.getProcessInstance();

        //这个同步很关键,避免多线程同时进入临界区
        synchronized (processInstance){

            super.enter(context, joinedPvmActivity);

            // 然后根据 blockId, 依次计算 fork gateway 的直接 outcoming transitions (需要从 join 环节反推，因为存在 unbalanced gateway) ,然后再减去未被激活的 transitions ，也就是 missed transitions

            // 不变式：countOfTheJoinLatch =  inComingPvmTransitions.size() -  missedPvmTransitions (因未满足条件进而未被触发的分支)

            // 如果reachedJoinCounter < countOfTheJoinLatch ，则继续等待
            // 如果reachedJoinCounter == countOfTheJoinLatch ，则触发 join 动作完成，驱动流程继续流转；
            // 如果reachedJoinCounter > countOfTheJoinLatch， 则报错


            ExecutionInstance joinedExecutionInstanceOfInclusiveGateway = context.getExecutionInstance();

            // 算法说明： 增加 blockId 字段，在 fork 环节完成设置；然后为本 fork 网关后续的每个 ei 设置 blockId （相同的blockId 标识他们在一个 fork-join 内 ） .这里的 blockId 为 fork gateway 的 instanceId ；
            ExecutionInstance forkedExecutionInstanceOfInclusiveGateway = findForkedExecutionInstance(context, joinedExecutionInstanceOfInclusiveGateway,   processEngineConfiguration,   executionInstanceStorage);
            PvmActivity forkedPvmActivity = getForkPvmActivity(processInstance, forkedExecutionInstanceOfInclusiveGateway,processEngineConfiguration);

            List<ExecutionInstance> allExecutionInstanceList = calcAllExecutionInstances(context, processInstance,  processEngineConfiguration,  executionInstanceStorage);

            //tune need cache activityIdList 是流程定义中，join配对的fork轨迹内部的所有的 activityId （由于存在 unbalanced gateway，会有 1个 fork，2个 join 这种情况  ）（与具体的流程实例无关）
            ActivityTreeNode activityTreeNode = buildActivityTreeFromJoinToFork(forkedPvmActivity, joinedPvmActivity);

            List<String> triggerActivityIds = calcTriggerActivityIds(context,variableInstanceStorage, forkedExecutionInstanceOfInclusiveGateway, variablePersister,  processEngineConfiguration);

            //tune need cache
            int     countOfTheJoinLatch = calcCountOfTheJoinLatch(activityTreeNode, triggerActivityIds);

            //当前内存中的，新产生的 active ExecutionInstance
            List<ExecutionInstance> activeExecutionList =   allExecutionInstanceList.stream()
                    .filter(ExecutionInstance::isActive).collect(Collectors.toList());

            return super.doa(context, gateway, processInstance, activeExecutionList, countOfTheJoinLatch, forkedExecutionInstanceOfInclusiveGateway);
        }
    }

    protected    void hookCleanUp(ExecutionContext context, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway) {
        if(null != forkedExecutionInstanceOfInclusiveGateway.getBlockId()){
            // 说明 forkedExecutionInstanceOfInclusiveGateway 是个嵌套网关,需要手动更新 context 的 blockId,然后方便后续join 网关识别出对应的 fork
            context.setBlockId(forkedExecutionInstanceOfInclusiveGateway.getBlockId());

        }
    }



    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_END);

            //fork
            List<PvmTransition> matchedTransitions = CommonGatewayHelper.calcMatchedTransitions(pvmActivity, context);

            putTriggerActivityIdsAndKeepForkContext(  pvmActivity,context, matchedTransitions);

            List<String> triggerActivityIds = (List<String>)context.getInnerExtra().get(buildTriggeredActivityIdKey(pvmActivity.getModel().getId()));

            // 暂停型流程实例,会丢失掉内存中的 context 数据,所以这里仍然需要持久化.
            persistMatchedTransitionsEagerly(  pvmActivity,context,  triggerActivityIds,  processEngineConfiguration,  variableInstanceStorage);

            CommonGatewayHelper.leaveAndConcurrentlyForkIfNeeded(context, pvmActivity,matchedTransitions);

        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            super.leave(context,pvmActivity);

        }

    }



}
