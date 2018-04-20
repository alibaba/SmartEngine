package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * Created by niefeng on 2018/4/18.
 */
public class InclusiveGatewayParser extends AbstractBpmnActivityParser<InclusiveGateway>
    implements StAXArtifactParser<InclusiveGateway> {

    public InclusiveGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected InclusiveGateway parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        InclusiveGateway inclusiveGateway = new InclusiveGateway();
        inclusiveGateway.setId(this.getString(reader, "id"));
        String sync = this.getString(reader, "sync");
        if(sync == null){
            inclusiveGateway.setSync(true);
        }else{
            inclusiveGateway.setSync(Boolean.parseBoolean(sync));
        }
        return inclusiveGateway;
    }

    @Override
    public QName getArtifactType() {
        return InclusiveGateway.type;
    }

    @Override
    public Class<InclusiveGateway> getModelType() {
        return InclusiveGateway.class;
    }
}
