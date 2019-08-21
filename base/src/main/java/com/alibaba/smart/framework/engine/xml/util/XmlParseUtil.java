package com.alibaba.smart.framework.engine.xml.util;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlParseUtil {

    public static String getString(XMLStreamReader reader, String name) {
        return reader.getAttributeValue((String)null, name);
    }

    public static boolean getBoolean(XMLStreamReader reader, String name) {
        return getBoolean(reader, name, false);
    }

    public static boolean getBoolean(XMLStreamReader reader, String name, boolean defaultValue) {
        String value = reader.getAttributeValue((String)null, name);
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
}