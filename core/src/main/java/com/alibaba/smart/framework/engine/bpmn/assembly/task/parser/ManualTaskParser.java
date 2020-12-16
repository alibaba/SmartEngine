package com.alibaba.smart.framework.engine.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.ManualTask;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ManualTask.class)
/**
 * @author zilong.jiangzl 2020-07-17
 */
public class ManualTaskParser extends AbstractBpmnParser<ManualTask> {

    @Override
    public Class<ManualTask> getModelType() {
        return ManualTask.class;
    }

    @Override
    public ManualTask parseModel(XMLStreamReader reader, ParseContext context) {
        ManualTask manualTask = new ManualTask();
        manualTask.setId(XmlParseUtil.getString(reader, "id"));
        manualTask.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        manualTask.setProperties(userTaskProperties);

        return manualTask;
    }

    @Override
    protected boolean parseModelChild(ManualTask model, BaseElement child) {

        return false;
    }

}
