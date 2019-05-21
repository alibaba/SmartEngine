package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ScriptTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午11:06
 */
public class ScriptTaskParser extends AbstractBpmnActivityParser<ScriptTask> implements StAXArtifactParser<ScriptTask> {

    public ScriptTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ScriptTask.type;
    }

    @Override
    public Class<ScriptTask> getModelType() {
        return ScriptTask.class;
    }

    @Override
    public ScriptTask parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
            ScriptTask scriptTask = new ScriptTask();
            scriptTask.setId(this.getString(reader, "id"));
            scriptTask.setName(this.getString(reader, "name"));


            Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
            scriptTask.setProperties(userTaskProperties);

            return scriptTask;
    }

}
