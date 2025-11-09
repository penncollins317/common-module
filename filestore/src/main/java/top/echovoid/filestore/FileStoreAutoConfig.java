package top.echovoid.filestore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import top.echovoid.filestore.handler.FileUploadHandler;
import top.echovoid.filestore.service.FileStoreService;
import top.echovoid.filestore.service.OssService;
import top.echovoid.filestore.service.impl.AliCloudOssService;
import top.echovoid.filestore.service.impl.FileSystemFileStoreService;
import top.echovoid.filestore.service.impl.MinioOssService;
import top.echovoid.filestore.service.impl.QiNiuYunOssService;
import top.echovoid.security.core.SecurityConfigProvider;

import java.util.Set;

import static org.springframework.web.servlet.function.RequestPredicates.POST;
import static org.springframework.web.servlet.function.RouterFunctions.route;

/**
 * @author Penn Collins
 * @since 2025/4/28
 */
@MapperScan("top.echovoid.filestore.mapper")
@Configuration
@ComponentScan
@EnableConfigurationProperties({FileStoreProperties.class, OssProps.class, FileSystemFileStoreService.FileSystemProps.class})
public class FileStoreAutoConfig {
    @Bean
    public FileUploadHandler fileUploadHandler() {
        return new FileUploadHandler();
    }

    @Bean
    public RouterFunction<ServerResponse> uploadRoute() {
        return route(POST("/upload/part"), this.fileUploadHandler());
    }

    @Bean
    @ConditionalOnMissingBean(FileStoreService.class)
    public FileStoreService fileSystemFileStoreService(FileSystemFileStoreService.FileSystemProps props) {
        return new FileSystemFileStoreService(props);
    }

    @Bean
    @ConditionalOnProperty(name = "echovoid.oss.type", havingValue = "minio")
    public OssService minioOssService(OssProps props) {
        return new MinioOssService(props);
    }

    @Bean
    @ConditionalOnProperty(name = "echovoid.oss.type", havingValue = "qiniu")
    public OssService qiniuOssService(OssProps props) {
        return new QiNiuYunOssService(props);
    }

    @Bean
    @ConditionalOnProperty(name = "echovoid.oss.type", havingValue = "ali")
    public OssService aliCloudOssService(OssProps props) {
        return new AliCloudOssService(props);
    }

    @Bean
    public SecurityConfigProvider filestoreSecurityConfigProvider() {
        return new SecurityConfigProvider() {
            @Override
            public Set<String> ignoreUrls() {
                return Set.of("/filestore/access/**");
            }
        };
    }
}
