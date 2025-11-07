package top.echovoid.common.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import top.echovoid.common.annotations.SearchField;
import top.echovoid.common.annotations.SortField;
import top.echovoid.common.params.PageParam;
import top.echovoid.common.params.PageExtendParam;
import top.echovoid.common.utils.DeepBeanUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2023/9/6
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> implements Serializable {
    /**
     * 分页数据
     */
    private List<T> records;

    /**
     * 总数据量
     */
    private long totalSize;

    /**
     * 总分页数
     */
    private long totalPage;


    public static <T> PageDTO<T> wrap(IPage<T> page) {
        return new PageDTO<>(page.getRecords(), page.getTotal(), page.getPages());
    }

    public static <T, D> PageDTO<D> wrap(IPage<T> page, Supplier<D> supplier) {
        return new PageDTO<>(DeepBeanUtil.copyProperties(page.getRecords(), supplier), page.getTotal(), page.getPages());
    }


    /**
     * 分页操作，直接使用实体类型
     *
     * @param baseMapper  基础Mapper
     * @param entityClass 实体类型
     * @param pageParam   分页参数
     * @param <T>         实体类型泛型
     * @return 分页DTO对象
     */
    public static <T> PageDTO<T> wrap(BaseMapper<T> baseMapper, Class<T> entityClass, PageParam pageParam) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        if (pageParam instanceof PageExtendParam PageExtendParam) {
            String[] fields = requireField(PageExtendParam.getFields(), entityClass);
            if (fields.length > 0) {
                queryWrapper.select(fields);
            }

            if (StringUtils.hasLength(PageExtendParam.getSearch())) {
                search(PageExtendParam.getSearch(), queryWrapper, entityClass);
            }

            order(PageExtendParam.getOrders(), queryWrapper, entityClass);
        }

        return handleResult(baseMapper.selectPage(new Page<>(pageParam.getPage(), pageParam.getSize()), queryWrapper));
    }

    public static <T, D> PageDTO<D> wrap(BaseMapper<T> baseMapper, Class<T> entityClass, PageParam pageParam, Supplier<D> supplier) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        if (pageParam instanceof PageExtendParam PageExtendParam) {
            String[] fields = requireField(PageExtendParam.getFields(), entityClass);
            if (fields.length > 0) {
                queryWrapper.select(fields);
            }

            if (StringUtils.hasLength(PageExtendParam.getSearch())) {
                search(PageExtendParam.getSearch(), queryWrapper, entityClass);
            }

            order(PageExtendParam.getOrders(), queryWrapper, entityClass);
        }

        Page<T> tPage = baseMapper.selectPage(new Page<>(pageParam.getPage(), pageParam.getSize()), queryWrapper);
        return new PageDTO<>(DeepBeanUtil.copyProperties(tPage.getRecords(), supplier), tPage.getTotal(), tPage.getPages());
    }

    public static <T> PageDTO<T> wrap(BaseMapper<T> baseMapper, Class<T> entityClass, PageParam pageParam, QueryWrapper<T> queryWrapper) {
        if (queryWrapper == null) {
            return wrap(baseMapper, entityClass, pageParam);
        }
        if (pageParam instanceof PageExtendParam PageExtendParam) {
            String[] fields = requireField(PageExtendParam.getFields(), entityClass);
            if (fields.length > 0) {
                queryWrapper.select(fields);
            }

            if (StringUtils.hasLength(PageExtendParam.getSearch())) {
                search(PageExtendParam.getSearch(), queryWrapper, entityClass);
            }

            order(PageExtendParam.getOrders(), queryWrapper, entityClass);
        }

        return handleResult(baseMapper.selectPage(new Page<>(pageParam.getPage(), pageParam.getSize()), queryWrapper));
    }

    public static <T, D> PageDTO<D> wrap(BaseMapper<T> baseMapper, Class<T> entityClass, PageParam pageParam, QueryWrapper<T> queryWrapper, Supplier<D> supplier) {
        if (pageParam instanceof PageExtendParam PageExtendParam) {
            String[] fields = requireField(PageExtendParam.getFields(), entityClass);
            if (fields.length > 0) {
                queryWrapper.select(fields);
            }

            if (StringUtils.hasLength(PageExtendParam.getSearch())) {
                search(PageExtendParam.getSearch(), queryWrapper, entityClass);
            }

            order(PageExtendParam.getOrders(), queryWrapper, entityClass);
        }

        Page<T> tPage = baseMapper.selectPage(new Page<>(pageParam.getPage(), pageParam.getSize()), queryWrapper);
        return new PageDTO<>(DeepBeanUtil.copyProperties(tPage.getRecords(), supplier), tPage.getTotal(), tPage.getPages());
    }

    private static <T> PageDTO<T> handleResult(IPage<T> result) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setRecords(result.getRecords());
        dto.setTotalPage(result.getPages());
        dto.setTotalSize(result.getTotal());
        return dto;
    }

    private static <T> void search(String searchKeyword, QueryWrapper<T> queryWrapper, Class<T> entityClass) {
        List<Field> searchFields = Arrays.stream(entityClass.getDeclaredFields()).filter(field -> field.getDeclaredAnnotation(SearchField.class) != null).toList();
        if (!searchFields.isEmpty() && StringUtils.hasLength(searchKeyword)) {
            for (int i = 0; i < searchFields.size(); i++) {
                if (i > 0) {
                    queryWrapper.or();
                }
                queryWrapper.like(searchFields.get(i).getName(), searchKeyword);
            }
        }
    }

    /**
     * 设置排序
     *
     * @param target       目标类
     * @param orderFields  排序字段规则
     * @param queryWrapper 目标QueryWrapper条件包装器
     * @param <T>          目标类泛型
     */
    private static <T> void order(Set<String> orderFields, QueryWrapper<T> queryWrapper, Class<T> target) {
        if (orderFields != null && !orderFields.isEmpty()) {
            for (String fieldName : orderFields) {
                boolean isAsc = isAsc(fieldName);
                if (!isAsc) {
                    fieldName = fieldName.substring(1);
                }
                try {
                    Field field = target.getDeclaredField(fieldName);
                    if (field.getAnnotation(SortField.class) != null) {
                        if (isAsc) {
                            queryWrapper.orderByAsc(fieldName);
                        } else {
                            queryWrapper.orderByAsc(fieldName);
                        }
                    }
                } catch (Exception exception) {
                    log.error(exception.getMessage());
                }
            }
        }
    }

    /**
     * 判断字段是否以负号开头
     *
     * @param fieldName 字段名称
     */
    private static boolean isAsc(String fieldName) {
        return fieldName.startsWith("-");
    }

    private static <T> String[] requireField(Set<String> fieldSet, Class<T> target) {
        if (fieldSet == null || fieldSet.isEmpty()) {
            return new String[]{};
        }
        return fieldSet.stream().map(field -> {
            try {
                return target.getDeclaredField(field).getName();
            } catch (Exception ignored) {
                return null;
            }
        }).filter(StringUtils::hasLength).toList().toArray(new String[]{});
    }
}
