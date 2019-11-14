package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionBasedElement;

import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnParser<M extends ExtensionBasedElement> extends AbstractElementParser<M> {


    @Override
    protected void singingMagic(M model, BaseElement child) {
        if(!this.parseModelChild(model, child)) {
            if (child instanceof ExtensionElements) {
                model.setExtensionElements((ExtensionElements)child);
            }


        }
    }


    /**
     *
     * @param model model
     * @param child child
     * @return circuit
     * @throws ParseException ParseException
     */
    protected boolean parseModelChild(M model, BaseElement child) throws ParseException {
        return false;
    }

    protected Map<String, String> parseExtendedProperties(XMLStreamReader reader, ParseContext context) {

        Map<String,String> properties = new HashMap();

        int attributeCount=reader.getAttributeCount();
        if(attributeCount>0){
            for (int i = 0; i < attributeCount; i++) {
                QName attributeName=reader.getAttributeName(i);

                String localPart = attributeName.getLocalPart();

                if("id".equals(localPart)||"name".equals(localPart)){
                    continue;
                }

                Object value=reader.getAttributeValue(attributeName.getNamespaceURI(), localPart);
                properties.put(localPart,(String)value);


            }
        }


        return properties;
    }
}
