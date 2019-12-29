package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.listener.EventListener;
import com.alibaba.smart.framework.engine.listener.EventListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;
import com.alibaba.smart.framework.engine.util.ClassLoaderUtil;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class ExecutionListener  implements Extension {
    public final static QName type = new QName(SmartBase.SMART_NS, "executionListener");

    private String[] events;
    private String listener;


    @Override
    public String getType() {
        return ExtensionElementsConstant.EXECUTION_LISTENER;
    }

    @Override
    public void decorate(ExtensionElements extensionElements) {
        EventListenerAggregation eventListenerAggregation =  (EventListenerAggregation)extensionElements.getDecorationMap().get(getType());

        if(null == eventListenerAggregation){
            eventListenerAggregation = new EventListenerAggregation();
            extensionElements.getDecorationMap().put(this.getType(),eventListenerAggregation);
        }

        for (String event : events) {

            EventListener listener = (EventListener)ClassLoaderUtil.createOrGetInstance(this.listener);

            Map<String, List<EventListener>> eventListenerMap = eventListenerAggregation.getEventListenerMap();

            List<EventListener> javaDelegationList = eventListenerMap.get(event);

                if(CollectionUtil.isNotEmpty(javaDelegationList)){
                    javaDelegationList.add(listener );

                }else{

                    javaDelegationList =CollectionUtil.newArrayList();
                    javaDelegationList.add(listener);
                    eventListenerMap.put(event,javaDelegationList);

                }

            }


    }




}
