package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
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

    @Override
    public void enter(ExecutionContext context) {


        ActivityBehavior behavior = this.getBehavior();
        boolean needPause= behavior.enter(context, this);

        fireEvent(context,PvmEventConstant.ACTIVITY_START);


        if (needPause) {

            // break;
            return;
        }

        this.execute(context);
    }

    protected void fireEvent(ExecutionContext context,PvmEventConstant event) {

        context.getProcessEngineConfiguration().getListenerExecutor().execute(event,this.getModel(),context);

    }

    @Override
    public void execute(ExecutionContext context) {


        this.getBehavior().execute(context,this);

        fireEvent(context,PvmEventConstant.ACTIVITY_EXECUTE);

        if (context.isNeedPause()) {

            // break;
            return;
        }

        fireEvent(context,PvmEventConstant.ACTIVITY_END);

        this.getBehavior().leave(context, this);

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
