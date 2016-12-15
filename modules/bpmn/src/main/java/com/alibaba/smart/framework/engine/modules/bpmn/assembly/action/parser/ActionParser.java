package com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author dongdong.zdd
 */
public class ActionParser extends AbstractStAXArtifactParser<Action> implements StAXArtifactParser<Action> {



    public ActionParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return Action.NameType;
    }

    @Override
    public Class<Action> getModelType() {
        return Action.class;
    }

    @Override
    public Action parse(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {

        Action action = new Action();
        action.setType(this.getStringThrowException(reader,"type"));
        action.setId(this.getStringThrowException(reader,"id"));
        action.setMethod(this.getStringThrowException(reader,"method"));
        action.setBean(this.getStringThrowException(reader,"bean"));

        while (this.nextChildElement(reader)) {

        }

        return action;

    }
}
