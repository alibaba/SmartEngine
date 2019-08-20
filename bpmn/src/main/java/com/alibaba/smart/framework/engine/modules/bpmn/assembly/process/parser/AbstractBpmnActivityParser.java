package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnActivityParser<M extends AbstractActivity> extends AbstractBpmnParser<M> {
    private final static String DEFAULT_ACTION = PvmEventConstant.ACTIVITY_EXECUTE.name();

    public AbstractBpmnActivityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected void parseChild(M model, BaseElement child) throws ParseException {
        if (child instanceof ExecutePolicy) {
            model.setExecutePolicy((ExecutePolicy)child);
        }else {
            super.parseChild(model, child);
        }
    }

    @Override
    protected String getDefaultActionName() {
        return DEFAULT_ACTION;
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
