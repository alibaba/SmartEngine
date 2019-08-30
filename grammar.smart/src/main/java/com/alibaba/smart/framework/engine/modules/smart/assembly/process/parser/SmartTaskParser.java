package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
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

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = SmartTask.class)

public class SmartTaskParser extends AbstractElementParser<SmartTask> {
    private final static String DEFAULT_ACTION = PvmEventConstant.ACTIVITY_EXECUTE.name();


    @Override
    protected SmartTask parseModel(XMLStreamReader reader, ParseContext context) {
        SmartTask smartTask = new SmartTask();
        smartTask.setId(XmlParseUtil.getString(reader, "id"));
        smartTask.setStartActivity(XmlParseUtil.getBoolean(reader, "isStart"));
        return smartTask;
    }

    @Override
    protected void singingMagic(SmartTask model, BaseElement child) {
        if (child instanceof Extensions) {
            model.setExtensions((Extensions)child);
        }

        //FIXME
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
        return SmartTask.type;
    }

    @Override
    public Class<SmartTask> getModelType() {
        return SmartTask.class;
    }
}
