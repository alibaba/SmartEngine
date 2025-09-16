package com.alibaba.smart.framework.engine.xml.util;

import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlParseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractElementParser.class);

    public static String getString(XMLStreamReader reader, String name) {
        return reader.getAttributeValue((String) null, name);
    }

    public static boolean getBoolean(XMLStreamReader reader, String name) {
        return getBoolean(reader, name, false);
    }

    public static boolean getBoolean(XMLStreamReader reader, String name, boolean defaultValue) {
        String value = reader.getAttributeValue((String) null, name);
        Boolean attr = value == null ? null : Boolean.valueOf(value);
        if (attr == null) {
            return defaultValue;
        } else {
            return attr;
        }
    }

    public static boolean nextChildElement(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();

            // LOGGER.debug(event + reader.getEventType() + "");

            if (event == XMLStreamConstants.END_ELEMENT) {
                return false;
            }
            if (event == XMLStreamConstants.START_ELEMENT) {
                return true;
            }
        }
        return false;
    }

    public static void skipToEndElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 0;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                ++depth;
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (depth == 0) {
                    return;
                }
                --depth;
            }
        }
    }

    public static Map<String, String> parseExtendedProperties(
            XMLStreamReader reader, ParseContext context) {

        Map<String, String> properties = new HashMap();

        int attributeCount = reader.getAttributeCount();
        if (attributeCount > 0) {
            for (int i = 0; i < attributeCount; i++) {
                QName attributeName = reader.getAttributeName(i);

                String localPart = attributeName.getLocalPart();

                Object value = reader.getAttributeValue(attributeName.getNamespaceURI(), localPart);
                properties.put(localPart, (String) value);
            }
        }

        return properties;
    }

    // public static void skipToStartElementEvent(XMLStreamReader reader) throws XMLStreamException
    // {
    //
    //    while (reader.hasNext()) {
    //
    //        int event = reader.next();
    //
    //        LOGGER.info(event + reader.getEventType() + "");
    //        if (event == XMLStreamConstants.START_ELEMENT) {
    //
    //            LOGGER.info("!!!BREAK!!!!"+event + reader.getEventType() + "");
    //
    //            break;
    //        }
    //
    //    }
    // }
    //
    // public static void skipToEndElementEvent(XMLStreamReader reader) throws XMLStreamException {
    //
    //    while (reader.hasNext()) {
    //
    //        int event = reader.next();
    //
    //        LOGGER.info(event + reader.getEventType() + "");
    //        if (event == XMLStreamConstants.END_ELEMENT) {
    //
    //            LOGGER.info("!!!BREAK!!!!"+event + reader.getEventType() + "");
    //
    //            break;
    //        }
    //
    //    }
    // }
}
