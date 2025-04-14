package top.mxzero.oss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.mxzero.oss.controller.OssPrepareUploadController;
import top.mxzero.oss.controller.OssUploadController;
import top.mxzero.oss.service.OssService;
import top.mxzero.oss.service.impl.AliCloudOssService;
import top.mxzero.oss.service.impl.MinioOssService;
import top.mxzero.oss.service.impl.QiNiuYunOssService;

@EnableConfigurationProperties(OssProps.class)
@Configuration
@ComponentScan
@MapperScan("top.mxzero.oss.mapper")
public class OSSAutoConfiguration {

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
    @ConditionalOnBean({OssService.class})
    public OssUploadController ossUploadController() {
        return new OssUploadController();
    }

    @Bean
    public OssPrepareUploadController ossPrepareUploadController() {
        return new OssPrepareUploadController();
    }
}
