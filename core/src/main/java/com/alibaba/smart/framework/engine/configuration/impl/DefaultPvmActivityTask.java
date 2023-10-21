package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.PvmActivityTask;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;



public  class DefaultPvmActivityTask implements PvmActivityTask {

        private PvmActivity pvmActivity;
        private ExecutionContext context;

        public DefaultPvmActivityTask(Object... args) {
            this.pvmActivity = (PvmActivity)args[0];
            this.context = (ExecutionContext)args[1];
        }


        @Override
        public PvmActivity call() {

            //忽略了子线程的返回值
            this.pvmActivity.enter(context);

            return null;

        }
    }
