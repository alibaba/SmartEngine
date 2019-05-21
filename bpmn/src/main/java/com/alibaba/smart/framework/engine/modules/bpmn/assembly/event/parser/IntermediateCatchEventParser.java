package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.IntermediateCatchEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午11:06
 */
public class IntermediateCatchEventParser extends AbstractBpmnActivityParser<IntermediateCatchEvent>
    implements StAXArtifactParser<IntermediateCatchEvent> {

    public IntermediateCatchEventParser(ExtensionPointRegistry extensionPointRegistry) { super(extensionPointRegistry); }

    @Override
    protected IntermediateCatchEvent parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
        intermediateCatchEvent.setId(this.getString(reader, "id"));
        intermediateCatchEvent.setStartActivity(true);
        return intermediateCatchEvent;
    }

    @Override
    public QName getArtifactType() {
        return IntermediateCatchEvent.type;
    }

    @Override
    public Class<IntermediateCatchEvent> getModelType() {
        return IntermediateCatchEvent.class;
    }

}
