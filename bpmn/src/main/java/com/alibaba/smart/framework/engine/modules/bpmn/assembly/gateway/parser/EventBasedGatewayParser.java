package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.EventBasedGateway;
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
public class EventBasedGatewayParser extends AbstractBpmnActivityParser<EventBasedGateway> implements StAXArtifactParser<EventBasedGateway> {

    public EventBasedGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return EventBasedGateway.type;
    }

    @Override
    public Class<EventBasedGateway> getModelType() {
        return EventBasedGateway.class;
    }

    @Override
    public EventBasedGateway parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
        EventBasedGateway eventBasedGateway = new EventBasedGateway();
        eventBasedGateway.setId(this.getString(reader, "id"));
        return eventBasedGateway;
    }

}
