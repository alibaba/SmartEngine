package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = ParallelGateway.class)

public class ParallelGatewayParser extends AbstractBpmnActivityParser<ParallelGateway>   {



    @Override
    public QName getQname() {
        return ParallelGateway.type;
    }

    @Override
    public Class<ParallelGateway> getModelType() {
        return ParallelGateway.class;
    }

    @Override
    public ParallelGateway parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {

        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(XmlParseUtil.getString(reader, "id"));
        return parallelGateway;
    }

}
