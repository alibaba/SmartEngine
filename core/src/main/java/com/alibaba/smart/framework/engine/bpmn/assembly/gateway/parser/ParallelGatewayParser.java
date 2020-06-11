package com.alibaba.smart.framework.engine.bpmn.assembly.gateway.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ParallelGateway.class)

public class ParallelGatewayParser extends AbstractBpmnParser<ParallelGateway> {



    @Override
    public QName getQname() {
        return ParallelGateway.type;
    }

    @Override
    public Class<ParallelGateway> getModelType() {
        return ParallelGateway.class;
    }

    @Override
    public ParallelGateway parseModel(XMLStreamReader reader, ParseContext context) {

        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(XmlParseUtil.getString(reader, "id"));
        parallelGateway.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        parallelGateway.setProperties(properties);

        return parallelGateway;
    }

}
