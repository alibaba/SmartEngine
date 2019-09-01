package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.ExecutionListener;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = ExecutionListener.class)

public class ExecutionListenerParser extends AbstractElementParser<ExecutionListener>
       {



    @Override
    protected ExecutionListener parseModel(XMLStreamReader reader, ParseContext context) {
        ExecutionListener executionListener = new ExecutionListener();
        String event = XmlParseUtil.getString(reader, "event");
        String listener = XmlParseUtil.getString(reader, "class");

        if (null != event) {
            String[] events = event.split(",");
            executionListener.setEvents(events);

            //FIXME 统一通过 instanceAccceor ,都是单例。
            executionListener.setListener(listener);
        }
        return executionListener;
    }

    @Override
    protected void singingMagic(ExecutionListener model, BaseElement child) {

        //fixme
        //if (child instanceof Performable) {
        //    model.setPerformable((Performable)child);
        //}
    }

    @Override
    public QName getQname() {
        return ExecutionListener.type;
    }

    @Override
    public Class<ExecutionListener> getModelType() {
        return ExecutionListener.class;
    }

}
