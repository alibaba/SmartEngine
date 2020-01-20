package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.parser;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.constant.SmartBase;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  20:53.
 */
public abstract class ClassNameWorkAroundUtil {

    public static String parse(XMLStreamReader reader){
        //no need add namespaceUri ,or using reader.getAttributeValue(2) (not good),see http://www.saxproject.org/namespaces.html,
        // http://stackoverflow.com/questions/7157355/what-is-the-difference-between-localname-and-qname,http://stackoverflow.com/questions/6390339/how-to-query-xml-using-namespaces-in-java-with-xpath


        //xml bug https://java.net/jira/browse/SJSXP-63 ,XML 解析有bug,所以这里兼容下。

        String className0 = reader.getAttributeValue(null,"class");
        if(null != className0){
            return  className0;
        }

        String className4 =   reader.getAttributeValue("","class");
        if(null != className4){
            return  className4;
        }

        String className3 =   reader.getAttributeValue(SmartBase.SMART_NS,"class");
        if(null != className3){
            return  className3;
        }

        String className1 = reader.getAttributeValue("smart","class");
        if(null != className1){
            return  className1;
        }


//        String className2 =   reader.getAttributeValue(2);
//        QName qName= reader.getAttributeName(2);
//        String namespaceURI =   qName.getNamespaceURI();
//        String localPart =  qName.getLocalPart();
//        String prefix  =  qName.getPrefix();
        return null;
    }
}
