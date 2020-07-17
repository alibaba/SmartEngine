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
 * @create 2020-07-16 9:42 下午
 */
@Data
public class ProcessField implements Extension {
    static String PROCESS_NS ="http://test.com/process";

    private static final long serialVersionUID = -5129848456612155165L;

    public final static QName type = new QName(PROCESS_NS, "field");

    private String name;
    private String value;
    private String valueType;

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

        map.put(this.getName(),this.getValue());

    }
}
