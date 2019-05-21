package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.IntermediateThrowEvent;
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
public class IntermediateThrowEventParser extends AbstractBpmnActivityParser<IntermediateThrowEvent>
    implements StAXArtifactParser<IntermediateThrowEvent> {

    public IntermediateThrowEventParser(ExtensionPointRegistry extensionPointRegistry) { super(extensionPointRegistry); }

    @Override
    protected IntermediateThrowEvent parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        IntermediateThrowEvent intermediateThrowEvent = new IntermediateThrowEvent();
        intermediateThrowEvent.setId(this.getString(reader, "id"));
        intermediateThrowEvent.setStartActivity(true);
        return intermediateThrowEvent;
    }

    @Override
    public QName getArtifactType() {
        return IntermediateThrowEvent.type;
    }

    @Override
    public Class<IntermediateThrowEvent> getModelType() {
        return IntermediateThrowEvent.class;
    }

}
