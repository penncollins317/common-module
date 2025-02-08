package top.mxzero.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.Map;

public class XmlUtils {
    private static final XmlMapper xmlMapper = new XmlMapper();

    // 将对象转换为 XML 字符串
    public static String toXml(Object object) {
        try {
            return xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Error converting object to XML", exception);
        }
    }

    // 将 XML 字符串转换为 Map
    public static Map<String, Object> xmlToMap(String xml) {
        try {
            return xmlMapper.readValue(xml, new TypeReference<>() {
            });
        } catch (IOException exception) {
            throw new RuntimeException("Error converting XML to Map", exception);
        }
    }

    // 将 XML 字符串转换为对象
    public static <T> T xmlToObject(String xml, Class<T> clazz) {
        try {
            return xmlMapper.readValue(xml, clazz);
        } catch (IOException exception) {
            throw new RuntimeException("Error converting XML to Object", exception);
        }
    }
}
