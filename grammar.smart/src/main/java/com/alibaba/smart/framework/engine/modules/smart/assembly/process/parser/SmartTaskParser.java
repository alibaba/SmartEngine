package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Extensions;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.SmartTask;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class SmartTaskParser extends AbstractElementParser<SmartTask> {
    private final static String DEFAULT_ACTION = PvmEventConstant.ACTIVITY_EXECUTE.name();

    public SmartTaskParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected SmartTask parseModel(XMLStreamReader reader, ParseContext context) {
        SmartTask smartTask = new SmartTask();
        smartTask.setId(XmlParseUtil.getString(reader, "id"));
        smartTask.setStartActivity(XmlParseUtil.getBoolean(reader, "isStart"));
        return smartTask;
    }

    @Override
    protected void parseSingleChild(SmartTask model, BaseElement child) {
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
    public QName getQname() {
        return SmartTask.type;
    }

    @Override
    public Class<SmartTask> getModelType() {
        return SmartTask.class;
    }
}
