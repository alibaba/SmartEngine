package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Extensions;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class SequenceFlowParser extends AbstractElementParser<SequenceFlow> {
    private final static String DEFAULT_ACTION = PvmEventConstant.TRANSITION_EXECUTE.name();

    public SequenceFlowParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected SequenceFlow parseModel(XMLStreamReader reader, ParseContext context) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(this.getString(reader, "id"));
        sequenceFlow.setSourceRef(this.getString(reader, "sourceRef"));
        sequenceFlow.setTargetRef(this.getString(reader, "targetRef"));
        return sequenceFlow;
    }

    @Override
    protected void parseChild(SequenceFlow model, BaseElement child) {
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
        return SequenceFlow.type;
    }

    @Override
    public Class<SequenceFlow> getModelType() {
        return SequenceFlow.class;
    }
}
