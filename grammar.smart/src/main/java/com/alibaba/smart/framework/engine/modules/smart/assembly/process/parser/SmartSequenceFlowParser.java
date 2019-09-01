package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;



import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = SmartSequenceFlow.class)

public class SmartSequenceFlowParser extends AbstractElementParser<SmartSequenceFlow> {
    private final static String DEFAULT_ACTION = PvmEventConstant.TRANSITION_EXECUTE.name();



    @Override
    protected SmartSequenceFlow parseModel(XMLStreamReader reader, ParseContext context) {
        SmartSequenceFlow smartSequenceFlow = new SmartSequenceFlow();
        smartSequenceFlow.setId(XmlParseUtil.getString(reader, "id"));
        smartSequenceFlow.setName(XmlParseUtil.getString(reader, "name"));

        smartSequenceFlow.setSourceRef(XmlParseUtil.getString(reader, "sourceRef"));
        smartSequenceFlow.setTargetRef(XmlParseUtil.getString(reader, "targetRef"));
        return smartSequenceFlow;
    }

    @Override
    protected void singingMagic(SmartSequenceFlow model, BaseElement child) {
        //TODO duplicate code
        //fixme

        if (child instanceof ExtensionElements) {
            model.setExtensions((ExtensionElements)child);
        }

        //else if (child instanceof Performable) {
        //    List<Performable> performers = model.getPerformers();
        //    if (null == performers) {
        //        performers = new ArrayList<Performable>();
        //        model.setPerformers(performers);
        //    }
        //    Performable performable = (Performable)child;
        //    if (null == performable.getAction() || "".equals(performable.getAction())) {
        //        performable.setAction(DEFAULT_ACTION);
        //    }
        //    performers.add(performable);
        //}
    }

    @Override
    public QName getQname() {
        return SmartSequenceFlow.type;
    }

    @Override
    public Class<SmartSequenceFlow> getModelType() {
        return SmartSequenceFlow.class;
    }
}
