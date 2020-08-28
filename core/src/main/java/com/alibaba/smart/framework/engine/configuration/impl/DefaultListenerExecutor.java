package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ListenerExecutor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.ContextBoundedJavaDelegation;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on 2020 August  17:52.
 */
public class DefaultListenerExecutor implements ListenerExecutor {

    public void execute(PvmEventConstant event, ExtensionElementContainer extensionElementContaine,ExecutionContext context) {
        String eventName = event.name();

        ExtensionElements extensionElements = extensionElementContaine.getExtensionElements();
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