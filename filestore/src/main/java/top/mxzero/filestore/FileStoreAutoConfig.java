package top.mxzero.filestore;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import top.mxzero.filestore.handler.FileUploadHandler;
import top.mxzero.filestore.service.FileStoreService;
import top.mxzero.filestore.service.FileSystemFileStoreService;
import top.mxzero.oss.mapper.FileMetaMapper;
import top.mxzero.security.core.SecurityConfigProvider;

import java.util.Set;

import static org.springframework.web.servlet.function.RequestPredicates.POST;
import static org.springframework.web.servlet.function.RouterFunctions.route;

/**
 * @author Peng
 * @since 2025/4/28
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties({FileStoreProperties.class, FileSystemFileStoreService.FileSystemProps.class})
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
    public FileStoreService fileSystemFileStoreService(FileSystemFileStoreService.FileSystemProps props, FileMetaMapper fileMetaMapper) {
        return new FileSystemFileStoreService(props, fileMetaMapper);
    }

    @Bean
    public SecurityConfigProvider filestoreSecurityConfigProvider(){
        return new SecurityConfigProvider() {
            @Override
            public Set<String> ignoreUrls() {
                return Set.of("/filestore/access/**");
            }
        };
    }
}
