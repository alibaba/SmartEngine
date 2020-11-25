package com.alibaba.smart.framework.engine.smart;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.constant.SmartBase;
import com.alibaba.smart.framework.engine.listener.ListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class ExecutionListener  implements ExtensionDecorator {
    public final static List<QName> qtypes = Arrays.asList(
            new QName(SmartBase.SMART_NS, "executionListener"),
            new QName(BpmnNameSpaceConstant.CAMUNDA_NAME_SPACE, "executionListener", "camunda"),
            new QName(BpmnNameSpaceConstant.FLOWABLE_NAME_SPACE, "executionListener", "flowable"),
            new QName(BpmnNameSpaceConstant.ACTIVITI_NAME_SPACE, "executionListener", "activiti")
    );

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
