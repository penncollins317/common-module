package top.mxzero.oss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.mxzero.oss.controller.FileUploadController;
import top.mxzero.oss.controller.OssPrepareUploadController;
import top.mxzero.oss.controller.OssUploadController;
import top.mxzero.oss.service.OssService;
import top.mxzero.oss.service.impl.*;

@EnableConfigurationProperties(OssProps.class)
@Configuration
@ComponentScan
@MapperScan("top.mxzero.oss.mapper")
public class OSSAutoConfiguration {
    @Bean
    @ConditionalOnProperty(name = "mxzero.oss.type", havingValue = "s3")
    public OssService s3OssClient(OssProps props) {
        return new S3OssService(props);
    }

    @Bean
    @ConditionalOnProperty(name = "mxzero.oss.type", havingValue = "minio")
    public OssService minioOssService(OssProps props) {
        return new MinioOssService(props);
    }

    @Bean
    @ConditionalOnProperty(name = "mxzero.oss.type", havingValue = "qiniu")
    public OssService qiniuOssService(OssProps props) {
        return new QiNiuYunOssService(props);
    }

    @Bean
    @ConditionalOnProperty(name = "mxzero.oss.type", havingValue = "ali")
    public OssService aliCloudOssService(OssProps props) {
        return new AliCloudOssService(props);
    }

    @Bean
    public FileRecordService fileRecordService() {
        return new FileRecordService();
    }

    @Bean
    @ConditionalOnBean({OssService.class, FileRecordService.class})
    public OssUploadController ossUploadController() {
        return new OssUploadController();
    }

    @Bean
    public FileUploadController fileUploadController() {
        return new FileUploadController();
    }

    @Bean
    public OssPrepareUploadController ossPrepareUploadController() {
        return new OssPrepareUploadController();
    }
}
