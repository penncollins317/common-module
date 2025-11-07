package top.echovoid.oss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.echovoid.oss.service.OssService;
import top.echovoid.oss.service.impl.AliCloudOssService;
import top.echovoid.oss.service.impl.MinioOssService;
import top.echovoid.oss.service.impl.QiNiuYunOssService;

@EnableConfigurationProperties(OssProps.class)
@Configuration
@ComponentScan
@MapperScan("top.echovoid.oss.mapper")
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
}
