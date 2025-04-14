package top.mxzero.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Peng
 * @since 2024/11/29
 */
public class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 允许未知字段
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 禁用时间戳格式
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 设置默认日期格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX"));
        // 设置时区为 GMT+8
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    private JsonUtils() {
    }

    public static String stringify(Object val) {
        try {
            return OBJECT_MAPPER.writeValueAsString(val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, new TypeReference<List<T>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> toMap(Object object) {
        try {
            return OBJECT_MAPPER.convertValue(object, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to map", e);
        }
    }

    public static Map<String, Object> parseMap(String jsonStr) {
        return parseMap(jsonStr, String.class, Object.class);
    }

    public static <T> Map<String, T> parseMap(String jsonStr, Class<T> valueClass) {
        return parseMap(jsonStr, String.class, valueClass);
    }

    public static <K, V> Map<K, V> parseMap(String jsonStr, Class<K> keyClass, Class<V> valueClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toEntity(Map<String, Object> map, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.convertValue(map, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error converting map to entity", e);
        }
    }
}
