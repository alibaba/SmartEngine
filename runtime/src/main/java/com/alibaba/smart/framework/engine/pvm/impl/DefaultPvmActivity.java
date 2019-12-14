package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.listener.EventListener;
import com.alibaba.smart.framework.engine.listener.EventListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultPvmActivity extends AbstractPvmActivity implements PvmActivity {

    protected ActivityInstanceFactory activityInstanceFactory;
    protected ExecutionInstanceFactory executionInstanceFactory;

    public DefaultPvmActivity(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
    }

    @Override
    public void enter(ExecutionContext context) {

        fireEvent(context,PvmEventConstant.ACTIVITY_START.name());

        boolean needPause= this.getBehavior().enter(context);

        //tune 删除setNeedPause？
        context.setNeedPause(needPause);
        //this.executePolicyBehavior.jump(this,context);

        if (context.isNeedPause()) {

            //FIXME why ??
            context.setNeedPause(false);
            // break;
            return;
        }

            //TODO ettear 以下逻辑待迁移到ExecutionPolicy中去
        this.execute(context);
    }

    private void fireEvent(ExecutionContext context,String event) {
        ExtensionElements extensionElements = this.getModel().getExtensionElements();
        if(null != extensionElements){

            EventListenerAggregation extension = (EventListenerAggregation)extensionElements.getDecorationMap().get(ExtensionElementsConstant.EXECUTION_LISTENER);

            if(null !=  extension){
                List<EventListener> listenerList = extension.getEventListenerMap().get(event);
                if(CollectionUtil.isNotEmpty(listenerList)){
                    for (EventListener listener : listenerList) {
                        listener.execute(context);
                    }
                }
            }


        }

    }

    @Override
    public void execute(ExecutionContext context) {
        fireEvent(context,PvmEventConstant.ACTIVITY_EXECUTE.name());


        this.getBehavior().execute(context);

        //tune 删除setNeedPause？
      //  context.setNeedPause(needPause);


        //this.executePolicyBehavior.execute(this,context);

        if (context.isNeedPause()) {
            context.setNeedPause(false);
            // break;
            return;
        }
        //TODO ettear 以下逻辑待迁移到ExecutionPolicy中去
        //this.invoke(PvmEventConstant.ACTIVITY_END.name(), context);
        //this.executeRecursively(context);

        fireEvent(context,PvmEventConstant.ACTIVITY_END.name());


        this.getBehavior().leave(this,context);

    }



    //private void executeRecursively(ExecutionContext context) {
    //
    //    //执行每个节点的hook方法
    //    Map<String, PvmTransition> outcomeTransitions = this.getOutcomeTransitions();
    //
    //    if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
    //        List<PvmTransition> matchedTransitions = new ArrayList<PvmTransition>(outcomeTransitions.size());
    //        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
    //            PvmTransition pendingTransition = transitionEntry.getValue();
    //            boolean matched = pendingTransition.match(context);
    //
    //            if (matched) {
    //                matchedTransitions.add(pendingTransition);
    //            }
    //
    //        }
    //        //TODO 针对互斥和并行网关的线要检验,返回值只有一个或者多个。如果无则抛异常。
    //
    //        for (PvmTransition matchedPvmTransition : matchedTransitions) {
    //            matchedPvmTransition.execute(context);
    //        }
    //    }
    //}



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
