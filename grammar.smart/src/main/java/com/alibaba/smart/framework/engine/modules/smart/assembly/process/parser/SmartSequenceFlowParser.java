package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Extensions;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class SmartSequenceFlowParser extends AbstractElementParser<SmartSequenceFlow> {
    private final static String DEFAULT_ACTION = PvmEventConstant.TRANSITION_EXECUTE.name();

    public SmartSequenceFlowParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected SmartSequenceFlow parseModel(XMLStreamReader reader, ParseContext context) {
        SmartSequenceFlow smartSequenceFlow = new SmartSequenceFlow();
        smartSequenceFlow.setId(this.getString(reader, "id"));
        smartSequenceFlow.setName(this.getString(reader, "name"));

        smartSequenceFlow.setSourceRef(this.getString(reader, "sourceRef"));
        smartSequenceFlow.setTargetRef(this.getString(reader, "targetRef"));
        return smartSequenceFlow;
    }

    @Override
    protected void parseChild(SmartSequenceFlow model, BaseElement child) {
        //TODO duplicate code

        if (child instanceof Extensions) {
            model.setExtensions((Extensions)child);
        }else if (child instanceof Performable) {
            List<Performable> performers = model.getPerformers();
            if (null == performers) {
                performers = new ArrayList<Performable>();
                model.setPerformers(performers);
            }
            Performable performable = (Performable)child;
            if (null == performable.getAction() || "".equals(performable.getAction())) {
                performable.setAction(DEFAULT_ACTION);
            }
            performers.add(performable);
        }
    }

    @Override
    public QName getArtifactType() {
        return SmartSequenceFlow.type;
    }

    @Override
    public Class<SmartSequenceFlow> getModelType() {
        return SmartSequenceFlow.class;
    }
}