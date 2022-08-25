package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ListenerExecutor;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

/**
 * Created by 高海军 帝奇 74394 on 2020 August  17:52.
 */
public class DefaultListenerExecutor implements ListenerExecutor {

    @Override
    public void execute(EventConstant event, ExtensionElementContainer extensionElementContainer, ExecutionContext context) {
        String eventName = event.name();

        ExtensionElements extensionElements = extensionElementContainer.getExtensionElements();
        if(null != extensionElements){

            ListenerAggregation extension = (ListenerAggregation)extensionElements.getDecorationMap().get(
                ExtensionElementsConstant.EXECUTION_LISTENER);

            if(null !=  extension){
                List<String> listenerClassNameList = extension.getEventListenerMap().get(eventName);
                if(CollectionUtil.isNotEmpty(listenerClassNameList)){
                    InstanceAccessor instanceAccessor = context.getProcessEngineConfiguration()
                        .getInstanceAccessor();
                    for (String listenerClassName : listenerClassNameList) {

                        Listener listener = (Listener)instanceAccessor.access(listenerClassName);
                        listener.execute(event, context);
                    }
                }
            }


        }

    }
}