package top.mxzero.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Peng
 * @since 2025/3/19
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return mybatisPlusInterceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                // createdAt
                if (metaObject.hasSetter("createdAt")) {
                    Class<?> type = metaObject.getGetterType("createdAt");
                    if (type.isAssignableFrom(Date.class)) {
                        this.strictInsertFill(metaObject, "createdAt", Date.class, new Date());
                    } else if (type.isAssignableFrom(LocalDateTime.class)) {
                        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
                    }
                }
                // updatedAt
                if (metaObject.hasSetter("updatedAt")) {
                    Class<?> type = metaObject.getGetterType("updatedAt");
                    if (type.isAssignableFrom(Date.class)) {
                        this.strictInsertFill(metaObject, "updatedAt", Date.class, new Date());
                    } else if (type.isAssignableFrom(LocalDateTime.class)) {
                        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
                    }
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                if (metaObject.hasSetter("updatedAt")) {
                    Class<?> type = metaObject.getGetterType("updatedAt");
                    if (type.isAssignableFrom(Date.class)) {
                        this.strictUpdateFill(metaObject, "updatedAt", Date.class, new Date());
                    } else if (type.isAssignableFrom(LocalDateTime.class)) {
                        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
                    }
                }
            }
        };
    }

}
