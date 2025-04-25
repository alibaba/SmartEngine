package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

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



        if (needPause) {

            // break;
            return;
        }

        this.execute(context);
    }



    @Override
    public void execute(ExecutionContext context) {


        this.getBehavior().execute(context,this);

        if (context.isNeedPause()) {

            // break;
            return;
        }

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
