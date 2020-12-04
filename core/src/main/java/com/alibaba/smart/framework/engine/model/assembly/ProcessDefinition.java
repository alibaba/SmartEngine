package com.alibaba.smart.framework.engine.model.assembly;

import java.util.List;
import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessDefinition extends ExtensionElementContainer,IdBasedElement {

    String getName();
    void setName(String name);


    String getVersion();
    void setVersion(String version);

    //String getIdAndVersion();
    //
    //void setIdAndVersion(String idAndVersion);
    

    List<BaseElement> getBaseElementList();

    void setBaseElementList( List<BaseElement> elementList);

    Map<String, IdBasedElement>  getIdBasedElementMap();

    void setIdBasedElementMap( Map<String, IdBasedElement> idBasedElementMap);

    void  setProperties(Map<String,String> properties);
}
