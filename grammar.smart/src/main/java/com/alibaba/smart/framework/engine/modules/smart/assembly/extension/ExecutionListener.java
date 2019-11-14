package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
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
    private String listener;//fixme 转成Listener这个对象，并且需要将context 提格。

    //FIXME 不必要每次实例化，统一处理机制，运行懒加载，或者托管。
    //private Map<String, List<EventListener>> map = new HashMap<String, List<EventListener>>();

    @Override
    public String getType() {
        return ExtensionElementsConstant.EXECUTION_LISTENER;
    }

    @Override
    public Object decorate(ExtensionElements extensionElements) {
        EventListenerAggregation eventListenerAggregation =  (EventListenerAggregation)extensionElements.getExtension(getType());

        if(null == eventListenerAggregation){
            eventListenerAggregation = new EventListenerAggregation();
        }

        for (String event : events) {

            JavaDelegation listener = (JavaDelegation)ClassLoaderUtil.createNewInstance(this.listener);

            Map<String, List<JavaDelegation>> eventListenerMap = eventListenerAggregation.getEventListenerMap();

            List<JavaDelegation> javaDelegationList = eventListenerMap.get(event);

                if(CollectionUtil.isNotEmpty(javaDelegationList)){
                    javaDelegationList.add(listener );

                }else{

                    javaDelegationList =CollectionUtil.newArrayList();
                    javaDelegationList.add(listener);
                    eventListenerMap.put(event,javaDelegationList);

                }

            }
        return eventListenerAggregation;
    }

}
