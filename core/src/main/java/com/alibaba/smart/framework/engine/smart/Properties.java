package com.alibaba.smart.framework.engine.smart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.constant.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class Properties implements ExtensionDecorator {
    public final static QName qtype = new QName(SmartBase.SMART_NS, "properties");

    private List<PropertiesElementMarker> extensionList  = new ArrayList();

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



        for (PropertiesElementMarker extensionDecorator : extensionList) {

            if(extensionDecorator instanceof  Value){
                Value value = (Value)extensionDecorator;
                map.put(value.getName(),value.getValue());
            }else if (extensionDecorator instanceof  Property){
                Property property = (Property)extensionDecorator;
                map.put(new PropertyCompositeKey(property.getType(),property.getName()),property.getValue());
            }

        }


    }



}
