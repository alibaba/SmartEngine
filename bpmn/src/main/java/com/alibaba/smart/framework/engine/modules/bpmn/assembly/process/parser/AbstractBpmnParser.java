package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Element;
import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;

import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnParser<M extends Element> extends AbstractElementParser<M> {


    @Override
    protected void singingMagic(M model, BaseElement child) throws ParseException {
        if(!this.parseModelChild(model, child)) {
            if (child instanceof Extensions) {
                model.setExtensions((Extensions)child);
            }

            //fixme
            //else if (child instanceof Performable) {
            //    List<Performable> performers = model.getPerformers();
            //    if (null == performers) {
            //        performers = new ArrayList<Performable>();
            //        model.setPerformers(performers);
            //    }
            //    Performable performable = (Performable)child;
            //    if (StringUtil.isEmpty(performable.getAction())) {
            //        performable.setAction(getDefaultActionName());
            //    }
            //    performers.add(performable);
            //}
        }
    }

    //@Override
    //protected void singingMagic(M model, BaseElement child) throws ParseException {
    //    if (child instanceof ExecutePolicy) {
    //        model.setExecutePolicy((ExecutePolicy)child);
    //    }else {
    //        super.singingMagic(model, child);
    //    }
    //}


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
