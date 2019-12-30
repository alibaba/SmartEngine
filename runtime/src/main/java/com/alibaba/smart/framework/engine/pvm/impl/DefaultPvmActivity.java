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
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
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


    @Override
    public void enter(ExecutionContext context) {

        fireEvent(context,PvmEventConstant.ACTIVITY_START.name());

        ActivityBehavior behavior = this.getBehavior();
        boolean needPause= behavior.enter(context);


        if (needPause) {

            // break;
            return;
        }

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

        if (context.isNeedPause()) {

            // break;
            return;
        }

        fireEvent(context,PvmEventConstant.ACTIVITY_END.name());

        this.getBehavior().leave(this,context);

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
