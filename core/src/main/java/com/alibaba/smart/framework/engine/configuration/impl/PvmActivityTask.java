package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.concurrent.Callable;

import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.GatewaySticker;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public  class PvmActivityTask implements Callable<PvmActivity> {
        private PvmActivity pvmActivity;
        private ExecutionContext context;

        public  PvmActivityTask(PvmActivity pvmActivity,ExecutionContext context) {
            this.pvmActivity = pvmActivity;
            this.context = context;
        }


        @Override
        public PvmActivity call() {

            PvmActivity pvmActivity ;
//            try {
//                GatewaySticker.create();

                //忽略了子线程的返回值
                this.pvmActivity.enter(context);

//                pvmActivity = GatewaySticker.currentSession().getPvmActivity();

//            }finally {
//
//                GatewaySticker.destroySession();
//            }

            return null;
//            return pvmActivity;


        }
    }
