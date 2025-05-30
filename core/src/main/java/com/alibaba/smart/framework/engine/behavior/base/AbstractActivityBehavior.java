package com.alibaba.smart.framework.engine.behavior.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.AbstractGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivityBehavior<T extends Activity> implements ActivityBehavior {



    @Setter
    protected ProcessInstanceFactory processInstanceFactory;
    @Setter
    protected ExecutionInstanceFactory executionInstanceFactory;
    @Setter
    protected ActivityInstanceFactory activityInstanceFactory;
    @Setter
    protected TaskInstanceFactory taskInstanceFactory;
    @Setter
    protected ProcessEngineConfiguration processEngineConfiguration;
    @Setter
    protected ExecutionInstanceStorage executionInstanceStorage;

    @Setter
    protected VariableInstanceStorage variableInstanceStorage;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractActivityBehavior.class);

    protected void fireEvent(ExecutionContext context,PvmActivity pvmActivity, EventConstant event) {

        context.getProcessEngineConfiguration().getListenerExecutor().execute(event,pvmActivity.getModel(),context);

    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        ProcessInstance processInstance = context.getProcessInstance();

        synchronized (processInstance){

            ActivityInstance activityInstance = createSingleActivityInstanceAndAttachToProcessInstance(context, pvmActivity.getModel());

            ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance, context);

            List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>(1);
            executionInstanceList.add(executionInstance);

            activityInstance.setExecutionInstanceList(executionInstanceList);
            context.setExecutionInstance(executionInstance);

            IdBasedElement idBasedElement = context.getProcessDefinition().getIdBasedElementMap().get(executionInstance.getProcessDefinitionActivityId());
            context.setBaseElement(idBasedElement);

            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_START);

            hookEnter(context,pvmActivity);

            return false;
        }

    }

    protected void hookEnter(ExecutionContext context, PvmActivity pvmActivity) {
        // default: do nothing

        // 意图: 目前仅包容网关需要，在首次生成包容网关 fork EI 的时候，需要将instanceId 赋值到 context 里面去

    }

    protected ActivityInstance createSingleActivityInstanceAndAttachToProcessInstance(ExecutionContext context, Activity activity) {
        ProcessInstance processInstance = context.getProcessInstance();

        ActivityInstance activityInstance = this.activityInstanceFactory.create(activity, context);
        processInstance.addActivityInstance(activityInstance);
        context.setActivityInstance(activityInstance);

        return activityInstance;
    }


    @Override
    public void execute(ExecutionContext context,PvmActivity pvmActivity) {

        hookExecute(context, pvmActivity);
        if (context.isNeedPause()) {
            // break;
            return;
        }

        fireEvent(context,pvmActivity, EventConstant.ACTIVITY_EXECUTE);

        executeDelegation(context,pvmActivity.getModel());

        commonUpdateExecutionInstance(context);

    }

    protected void hookExecute(ExecutionContext context, PvmActivity pvmActivity) {
        // default: do nothing

        // 意图: 用于解决页面上多个按钮,当用户点击了这个按钮,却不触发业务流程的状态的变化,仅仅是改变业务领域对象.
        // 所以,这里提供了一个扩展机制,允许ClientProgrammer 在仍然使用signal 方法的时候,但是不驱动流程变化. 一般业务忽略此方法即可.
        // 一般示例: 传入 开启选项参数 和 按钮名称, 然后反射调用对应的类执行业务逻辑
    }


    protected void commonUpdateExecutionInstance(ExecutionContext context) {
        if (!context.isNeedPause()) {
            ExecutionInstance executionInstance = context.getExecutionInstance();
            MarkDoneUtil.markDoneExecutionInstance(executionInstance,executionInstanceStorage,
                processEngineConfiguration);
        }
    }

    protected void executeDelegation(ExecutionContext context, Activity activity) {

        context.getProcessEngineConfiguration().getDelegationExecutor().execute(context, activity);
    }


    /**
     * dead or alive ,maybe change later...
     * @param context
     * @param gateway
     * @param processInstance
     * @param activeExecutionList
     * @param countOfTheJoinLatch
     * @param forkedExecutionInstanceOfInclusiveGateway
     * @return
     */
    protected boolean doa(ExecutionContext context, AbstractGateway gateway, ProcessInstance processInstance, List<ExecutionInstance> activeExecutionList, int countOfTheJoinLatch, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway) {
        List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);

        List<ExecutionInstance> mergedExecutionInstanceList = new ArrayList<ExecutionInstance>();

        mergedExecutionInstanceList.addAll(executionInstanceListFromMemory);

        for (ExecutionInstance executionInstance : activeExecutionList) {
            if(mergedExecutionInstanceList.contains(executionInstance)){
                //do nothing
            }else {
                mergedExecutionInstanceList.add(executionInstance);
            }
        }

        int reachedJoinCounter = 0;
        List<ExecutionInstance> chosenExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());

        if(null != mergedExecutionInstanceList){

            for (ExecutionInstance executionInstance : mergedExecutionInstanceList) {

                if (executionInstance.getProcessDefinitionActivityId().equals(gateway.getId())) {
                    reachedJoinCounter++;
                    chosenExecutionInstanceList.add(executionInstance);
                }
            }
        }


        LOGGER.debug("chosenExecutionInstanceList , reachedJoinCounter,countOfTheJoinLatch  is {} , {} , {} ",chosenExecutionInstanceList,reachedJoinCounter, countOfTheJoinLatch);
        if(reachedJoinCounter > countOfTheJoinLatch){
            throw new EngineException("Unexpected behavior,reachedJoinCounter: " + reachedJoinCounter +",countOfTheJoinLatch: "+ countOfTheJoinLatch);
        }
        else if(reachedJoinCounter == countOfTheJoinLatch){
            //把当前停留在join节点的执行实例全部complete掉,然后再持久化时,会自动忽略掉这些节点。

            if(null != chosenExecutionInstanceList){
                for (ExecutionInstance executionInstance : chosenExecutionInstanceList) {
                    MarkDoneUtil.markDoneExecutionInstance(executionInstance,executionInstanceStorage,
                            processEngineConfiguration);
                }
            }

            // 虽然 fork-join 对已经结束,但是 unbalanced 这种场景还需要 blockId 去计算
            // context.setBlockId(null);

            hookCleanUp(context, forkedExecutionInstanceOfInclusiveGateway);

            return false;

        }else{
            //未完成的话,流程继续暂停
            return true;
        }
    }

    protected    void hookCleanUp(ExecutionContext context, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway) {

    }



    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {
        fireEvent(context,pvmActivity, EventConstant.ACTIVITY_END);


        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();

        if(MapUtil.isEmpty(outcomeTransitions)){
            LOGGER.debug("No outcomeTransitions found for activity id: "+pvmActivity.getModel().getId()+", it's just fine, it should be the last activity of the process");

        }else{

            if( outcomeTransitions.size() ==1){
                for (Entry<String, PvmTransition> pvmTransitionEntry : outcomeTransitions.entrySet()) {
                    PvmActivity target = pvmTransitionEntry.getValue().getTarget();
                    target.enter(context);
                }
            }else {

                 CommonGatewayHelper.chooseOnlyOne(pvmActivity,context);

            }
        }

    }






}
