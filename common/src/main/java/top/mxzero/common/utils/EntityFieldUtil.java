package top.mxzero.common.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/3/8
 */
public class EntityFieldUtil {
    private EntityFieldUtil() {

    }

    /**
     * 获取目标类中存在该字段
     *
     * @param fields 字段列表
     * @param target 目标对象
     */
    public Set<String> validateFields(Set<String> fields, Class<?> target) {
        fields.retainAll(Arrays.stream(target.getDeclaredFields()).collect(Collectors.toSet()));
        return fields;
    }


}
