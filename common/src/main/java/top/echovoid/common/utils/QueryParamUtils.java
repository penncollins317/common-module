package top.echovoid.common.utils;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peng
 * @since 2024/10/4
 */
public class QueryParamUtils {
    private QueryParamUtils() {
    }

    public static Map<String, String> parseParams(String queryString) {
        Map<String, String> params = new HashMap<>();

        if (StringUtils.hasLength(queryString)) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                // 拆分键和值
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    params.put(key, value);
                }
            }
        }

        return params;
    }
}
