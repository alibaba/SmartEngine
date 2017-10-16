package com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionChecker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.AbortCheckPerformable;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.Collection;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance
    .CompletionCheckPerformable;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance
    .CompletionCheckPreparePerformable;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXAttributeParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class CollectionParser extends AbstractStAXArtifactParser<Collection> implements
    StAXAttributeParser<Collection> {

    public CollectionParser(
        ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public Collection parse(QName attributeName, XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        Collection collection = new Collection();

        Object element=context.getCurrentElement();
        if(null!=element && element instanceof MultiInstanceLoopCharacteristics){
            MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics=(MultiInstanceLoopCharacteristics)element;

            multiInstanceLoopCharacteristics.setCompletionCheckPrepare(new CompletionCheckPreparePerformable());

            //创建一个All模式的Checker，不是All模式会被覆盖
            CompletionChecker completionChecker=new CompletionChecker();
            completionChecker.setAbortCheckPerformable(new AbortCheckPerformable());
            completionChecker.setCompletionCheckPerformable(new CompletionCheckPerformable());
            multiInstanceLoopCharacteristics.setCompletionChecker(completionChecker);
        }
        return collection;
    }

    @Override
    public QName getArtifactType() {
        return Collection.type;
    }

    @Override
    public Class<Collection> getModelType() {
        return Collection.class;
    }
}