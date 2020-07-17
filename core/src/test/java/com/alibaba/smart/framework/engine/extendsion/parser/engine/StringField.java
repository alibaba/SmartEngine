package com.alibaba.smart.framework.engine.extendsion.parser.engine;

import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import lombok.Data;

/**
 * @author zilong.jiangzl
 * @create 2020-07-16 11:41 下午
 */
@Data
public class StringField implements Extension {
    static String PROCESS_NS ="http://test.com/process";

    private static final long serialVersionUID = -5129848456612155165L;

    public final static QName type = new QName(PROCESS_NS, "string");

    private String value;

    @Override
    public String getType() {
        return ExtensionElementsConstant.PROPERTIES;
    }

    @Override
    public void decorate(ExtensionElements extensionElements) {
        Map map =  (Map)extensionElements.getDecorationMap().get(getType());

        if(null == map){
            map = MapUtil.newHashMap();
            extensionElements.getDecorationMap().put(this.getType(),map);
        }

        map.put("value",this.getValue());
    }
}


/*
    static final String ATTRIBUTE_STRING_VALUE = "stringValue";
    static final String ATTRIBUTE_JSON_VALUE = "jsonValue";
    static final String ATTRIBUTE_INTEGER_VALUE = "integerValue";
    static final String ATTRIBUTE_BOOLEAN_VALUE = "booleanValue";
    static final String ATTRIBUTE_LONG_VALUE = "longValue";

 */