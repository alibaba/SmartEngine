package com.alibaba.smart.framework.engine.smart;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.constant.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class Value  implements PropertiesElementMarker, ExtensionDecorator {
    public final static List<QName> qtypes = Arrays.asList(
            new QName(SmartBase.SMART_NS, "value"),
            new QName(BpmnNameSpaceConstant.CAMUNDA_NAME_SPACE, "value", "camunda"),
            new QName(BpmnNameSpaceConstant.FLOWABLE_NAME_SPACE, "value", "flowable"),
            new QName(BpmnNameSpaceConstant.ACTIVITI_NAME_SPACE, "value", "activiti")
    );

    private String name;
    private String value;

    @Override
    public String getDecoratorType() {
        return ExtensionElementsConstant.PROPERTIES;
    }

    @Override
    public void decorate(ExtensionElements extensionElements) {

        Map map =  (Map)extensionElements.getDecorationMap().get(getDecoratorType());

        if(null == map){
            map = MapUtil.newHashMap();
            extensionElements.getDecorationMap().put(this.getDecoratorType(),map);
        }

        map.put(this.getName(),this.getValue());

    }

}
