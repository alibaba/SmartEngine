package com.alibaba.smart.framework.engine.smart;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
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
    public void decorate(ExtensionElements extensionElements, ParseContext context) {
        ListenerAggregation eventListenerAggregation =  (ListenerAggregation)extensionElements.getDecorationMap().get(
            getDecoratorType());

        if(null == eventListenerAggregation){
            eventListenerAggregation = new ListenerAggregation();
            extensionElements.getDecorationMap().put(this.getDecoratorType(),eventListenerAggregation);
        }

        for (String event : events) {

            // 兼容主流
            if( EventConstant.start.name().equals(event)){
                if(context.getParent().getCurrentElement() instanceof ProcessDefinitionSource){

                    event = EventConstant.PROCESS_START.name();
                }else {
                    event = EventConstant.ACTIVITY_START.name();
                }

            }else if (EventConstant.end.name().equals(event)){

                if(context.getParent().getCurrentElement() instanceof ProcessDefinitionSource){
                    event = EventConstant.PROCESS_END.name();
                }else {
                    event = EventConstant.ACTIVITY_END.name();
                }

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
