package com.alibaba.smart.framework.engine.extendsion.parser.engine;

import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import lombok.Data;

/**
 * @author zilong.jiangzl
 * @create 2020-07-16 9:42 下午
 * please use @com.alibaba.smart.framework.engine.smart.Property, Example: com.alibaba.smart.framework.engine.test.cases.extensions.CompositePropertiesTest
 */
@Data
@Deprecated
public class ProcessField implements ExtensionDecorator {
    static String PROCESS_NS ="http://test.com/process";

    private static final long serialVersionUID = -5129848456612155165L;

    public final static QName qtype = new QName(PROCESS_NS, "field");

    private String name;
    private String value;
    private String valueType;

    @Override
    public String getDecoratorType() {
        return ExtensionElementsConstant.PROPERTIES;
    }

    @Override
    public void decorate(ExtensionElements extensionElements, ParseContext context) {
        Map map = (Map)extensionElements.getDecorationMap().get(getDecoratorType());

        if (null == map) {
            map = MapUtil.newHashMap();
            extensionElements.getDecorationMap().put(this.getDecoratorType(), map);
        }

        map.put(this.getName(), this.getValue());
    }
}
