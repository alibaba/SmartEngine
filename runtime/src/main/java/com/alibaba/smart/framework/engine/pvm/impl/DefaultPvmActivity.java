package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.common.util.ThreadPoolUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.impl.DefaultInstanceContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.provider.ExecutePolicyBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPvmActivity extends AbstractPvmActivity implements PvmActivity {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPvmActivity.class);

    protected ActivityInstanceFactory activityInstanceFactory;
    protected ExecutionInstanceFactory executionInstanceFactory;
    private ExecutePolicyBehavior executePolicyBehavior;
    private static ExecutorService executorService = ThreadPoolUtil.createNewDefaultThreadPool("PVMSlaveProcessPool");
    private static final ReentrantLock LOCK = new ReentrantLock();

    public DefaultPvmActivity(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
    }

    @Override
    public void enter(ExecutionContext context) {
        this.buildInstanceRelationShip(context);
        this.executePolicyBehavior.enter(this, context);

        if (context.isNeedPause()) {

            //FIXME why ??
            context.setNeedPause(false);
            // break;
            return;
        }

        //TODO ettear 以下逻辑待迁移到ExecutionPolicy中去
        this.execute(context);
    }

    @Override
    public void execute(ExecutionContext context) {
        this.executePolicyBehavior.execute(this, context);

        if (context.isNeedPause()) {
            context.setNeedPause(false);
            // break;
            return;
        }
        //TODO ettear 以下逻辑待迁移到ExecutionPolicy中去
        this.invoke(PvmEventConstant.ACTIVITY_END.name(), context);
        this.executeRecursively(context);
    }

    private void buildInstanceRelationShip(ExecutionContext context) {
        ProcessInstance processInstance = context.getProcessInstance();

        ActivityInstance activityInstance = this.activityInstanceFactory.create(this.getModel(), context);
        //ExecutionInstance executionInstance = this.executionInstanceFactory.create(activityInstance,  context);
        //
        //activityInstance.setExecutionInstance(executionInstance);
        if (context instanceof DefaultInstanceContext && ((DefaultInstanceContext)context).isNeedParallelLock()) {
            LOCK.lock();
            try {
                processInstance.addActivityInstance(activityInstance);
            } finally {
                LOCK.unlock();
            }
        } else {
            processInstance.addActivityInstance(activityInstance);
        }
        //
        //context.setExecutionInstance(executionInstance);
        context.setActivityInstance(activityInstance);
    }

    private void executeRecursively(ExecutionContext context) throws EngineException {
        //执行每个节点的hook方法
        Map<String, PvmTransition> outcomeTransitions = this.getOutcomeTransitions();

        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pendingTransition = transitionEntry.getValue();
                boolean matched = pendingTransition.match(context);

                if (matched) {
                    matchedTransitions.add(pendingTransition);
                }

            }
            //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。

            if (matchedTransitions.isEmpty()) {
                return;
            }
            if (matchedTransitions.size() == 1) {
                matchedTransitions.iterator().next().execute(context);
            } else {
                List<PvmTransitionTask> tasks = new ArrayList<PvmTransitionTask>();
                for (PvmTransition pvmTransition : matchedTransitions) {
                    tasks.add(new PvmTransitionTask(pvmTransition, context));
                }
                try {
                    executorService.invokeAll(tasks);
                } catch (InterruptedException e) {
                    throw new EngineException(e.getMessage(), e);
                }
            }
        }
    }

    class PvmTransitionTask implements Callable<Void> {
        private PvmTransition pvmTransition;
        private DefaultInstanceContext context;

        PvmTransitionTask(PvmTransition pvmTransition, ExecutionContext context) {
            this.pvmTransition = pvmTransition;
            this.context = DefaultInstanceContext.copy(context);
            this.context.setNeedParallelLock(true);
        }

        @Override
        public Void call() {
            try {
                pvmTransition.execute(context);
            } catch (Exception e) {
                //  某个分支抛异常了怎么处理
                LOGGER.error("ERROR: " + e.getMessage(), e);
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return " [id=" + getModel().getId() + "]";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
