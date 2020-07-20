package com.alibaba.smart.framework.engine.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.ScriptTask;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ScriptTask.class)
/**
 * @author zilong.jiangzl 2020-07-17
 */
public class ScriptTaskParser extends AbstractBpmnParser<ScriptTask> {


    @Override
    public QName getQname() {
        return ScriptTask.type;
    }

    @Override
    public Class<ScriptTask> getModelType() {
        return ScriptTask.class;
    }

    @Override
    public ScriptTask parseModel(XMLStreamReader reader, ParseContext context) {
        ScriptTask scripttask = new ScriptTask();
        scripttask.setId(XmlParseUtil.getString(reader, "id"));
        scripttask.setName(XmlParseUtil.getString(reader, "name"));

        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        scripttask.setProperties(userTaskProperties);

        return scripttask;
    }

    @Override
    protected boolean parseModelChild(ScriptTask model, BaseElement child) {

        return false;
    }

}
