package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;
import com.alibaba.smart.framework.engine.util.ClassUtil;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class ExecutionListener  implements Extension {
    public final static QName type = new QName(SmartBase.SMART_NS, "executionListener");

    private String[] events;
    private String listenerClass;


    @Override
    public String getType() {
        return ExtensionElementsConstant.EXECUTION_LISTENER;
    }

    @Override
    public void decorate(ExtensionElements extensionElements) {
        ListenerAggregation eventListenerAggregation =  (ListenerAggregation)extensionElements.getDecorationMap().get(getType());

        if(null == eventListenerAggregation){
            eventListenerAggregation = new ListenerAggregation();
            extensionElements.getDecorationMap().put(this.getType(),eventListenerAggregation);
        }

        for (String event : events) {

            //Listener listener = (Listener)ClassUtil.createOrGetInstance(this.listenerClass);

            Map<String, List<String>> eventListenerMap = eventListenerAggregation.getEventListenerMap();

            List<String> listenerList = eventListenerMap.get(event);

                if(CollectionUtil.isNotEmpty(listenerList)){
                    listenerList.add(this.listenerClass );
                }else{
                    listenerList =CollectionUtil.newArrayList();
                    listenerList.add(this.listenerClass);
                    eventListenerMap.put(event,listenerList);
                }

            }


    }




}
