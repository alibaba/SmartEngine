package com.alibaba.smart.framework.engine.smart.parser;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.smart.ExecutionListener;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ExecutionListener.class)
public class ExecutionListenerParser extends AbstractElementParser<ExecutionListener> {

    @Override
    protected ExecutionListener parseModel(XMLStreamReader reader, ParseContext context) {
        String event = XmlParseUtil.getString(reader, "event");
        String listener = XmlParseUtil.getString(reader, "class");

        if (null != event) {
            ExecutionListener executionListener = new ExecutionListener();

            String[] events = event.split(",");
            executionListener.setEvents(events);

            executionListener.setListenerClass(listener);
            return executionListener;
        }else{
            throw new EngineException("Events can not be empty for ExecutionListener");
        }
    }

    @Override
    public Class<ExecutionListener> getModelType() {
        return ExecutionListener.class;
    }

}
