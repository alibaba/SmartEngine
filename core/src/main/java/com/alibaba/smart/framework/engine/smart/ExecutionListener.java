package com.alibaba.smart.framework.engine.smart;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class ExecutionListener  implements ExtensionDecorator,CustomExtensionElement {

    public final static String xmlLocalPart = "executionListener";

    private String[] events;
    private String listenerClass;


    @Override
    public String getDecoratorType() {
        return ExtensionElementsConstant.EXECUTION_LISTENER;
    }

    @Override
    public void decorate(ExtensionElements extensionElements) {
        ListenerAggregation eventListenerAggregation =  (ListenerAggregation)extensionElements.getDecorationMap().get(
            getDecoratorType());

        if(null == eventListenerAggregation){
            eventListenerAggregation = new ListenerAggregation();
            extensionElements.getDecorationMap().put(this.getDecoratorType(),eventListenerAggregation);
        }

        for (String event : events) {

            // 兼容主流
            if("start".equals(event)){
                event = "ACTIVITY_START";
            }else if ("end".equals(event)){
                event = "ACTIVITY_END";
            }


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
