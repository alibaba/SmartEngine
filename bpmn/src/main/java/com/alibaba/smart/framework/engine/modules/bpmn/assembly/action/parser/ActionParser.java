package com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by dongdongzdd on 16/9/8.
 */
public class ActionParser extends AbstractElementParser<Action> implements ElementParser<Action> {


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
        action.setType(this.getString(reader, "type"));
        action.setId(this.getString(reader, "id"));
        action.setMethod(this.getString(reader, "method"));

        while (this.nextChildElement(reader)) {

        }

        return action;

    }
}
