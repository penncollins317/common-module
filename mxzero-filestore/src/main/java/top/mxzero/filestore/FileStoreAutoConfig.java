package top.mxzero.filestore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import top.mxzero.filestore.handler.FileUploadHandler;

import static org.springframework.web.servlet.function.RequestPredicates.POST;
import static org.springframework.web.servlet.function.RouterFunctions.route;

/**
 * @author Peng
 * @since 2025/4/28
 */
@Configuration
@ComponentScan
public class FileStoreAutoConfig {
    @Bean
    public FileUploadHandler fileUploadHandler() {
        return new FileUploadHandler();
    }           

    @Bean
    public RouterFunction<ServerResponse> uploadRoute() {
        return route(POST("/upload/part"), this.fileUploadHandler());
    }
}
