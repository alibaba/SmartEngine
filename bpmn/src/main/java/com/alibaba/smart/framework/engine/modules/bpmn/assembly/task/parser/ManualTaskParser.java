package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ManualTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午11:06
 */
public class ManualTaskParser extends AbstractBpmnActivityParser<ManualTask> implements StAXArtifactParser<ManualTask> {

    public ManualTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ManualTask.type;
    }

    @Override
    public Class<ManualTask> getModelType() {
        return ManualTask.class;
    }

    @Override
    public ManualTask parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
            ManualTask manualTask = new ManualTask();
            manualTask.setId(this.getString(reader, "id"));
            manualTask.setName(this.getString(reader, "name"));


            Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
            manualTask.setProperties(userTaskProperties);

            return manualTask;
    }

}
