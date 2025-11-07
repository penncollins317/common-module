package top.echovoid.common.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Peng
 * @email penncollins317@gmail.com
 * @since 2023/9/1
 */
public class DeepBeanUtil extends BeanUtils {
    private DeepBeanUtil() {
    }


    /**
     * 多个对象属性拷贝
     *
     * @param source 源对象集合
     * @param target 目标对象函数接口
     * @param <T>    目标对象类型
     * @return 返回目标对象集合
     */
    public static <T> List<T> copyProperties(List<?> source, Supplier<T> target) {
        return source.stream().map(data -> {
            T t = target.get();
            BeanUtils.copyProperties(data, t);
            return t;
        }).toList();
    }

    /**
     * 单个对象属性拷贝
     *
     * @param source 源对象
     * @param target 目标对象函数接口
     * @param <T>    目标对象类型
     * @return 返回目标对象
     */
    public static <T> T copyProperties(Object source, Supplier<T> target) {
        if (source == null) {
            return null;
        }

        T instance = target.get();
        BeanUtils.copyProperties(source, instance);
        return instance;
    }

    /**
     * 单个对象属性拷贝
     *
     * @param source 源对象
     * @param clazz  目标对象函数接口
     * @param <T>    目标对象类型
     * @return 返回目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 多个对象属性拷贝
     *
     * @param source 源对象集合
     * @param clazz  目标对象函数接口
     * @param <T>    目标对象类型
     * @return 返回目标对象集合
     */
    public static <T> List<T> copyProperties(List<?> source, Class<T> clazz) {
        return source.stream().map(data -> copyProperties(data, clazz)).toList();
    }
}
