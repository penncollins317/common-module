package top.mxzero.oss.service.impl;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import top.mxzero.oss.OssClientType;
import top.mxzero.oss.OssProps;
import top.mxzero.oss.dto.OssUploadResult;
import top.mxzero.oss.service.OssService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Peng
 * @since 2024/9/21
 */
@Slf4j
public class AliCloudOssService implements OssService {
    private final OssProps props;
    private final OSS client;

    public AliCloudOssService(OssProps props) {
        this.props = props;
        this.client = OSSClientBuilder.create()
                .endpoint(props.getEndpoint())
                .credentialsProvider(new DefaultCredentialProvider(props.getAccessKey(), props.getSecretKey()))
                .build();
    }

    @Override
    public OssUploadResult upload(byte[] data, String filename, String contentType) {
        return this.upload(new ByteArrayInputStream(data), filename, contentType, data.length);
    }

    @Override
    public OssUploadResult upload(InputStream inputStream, String filename, String contentType, long size) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        try {
            byte[] buffer = inputStream.readAllBytes();
            PutObjectResult putObjectResult = this.client.putObject(new PutObjectRequest(this.props.getBucketName(), filename, new ByteArrayInputStream(buffer), metadata));
            log.info("{}", putObjectResult);
            return OssUploadResult.builder()
                    .type(OssClientType.ALI)
                    .key(filename)
                    .contentType(contentType)
                    .size(buffer.length)
                    .bucketName(this.props.getBucketName())
                    .url(this.prefixName() + filename)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String filename) {
        return this.client.doesObjectExist(this.props.getBucketName(), filename);
    }

    @Override
    public boolean remove(String filename) {
        VoidResult result = this.client.deleteObject(this.props.getBucketName(), filename);
        return result.getResponse().isSuccessful();
    }

    @Override
    public String prepareSign(String name) {
        try {
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L);
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(this.props.getBucketName(), name, HttpMethod.PUT);
            request.setExpiration(expiration);
            return this.client.generatePresignedUrl(request).toString();
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:{}", oe.getErrorMessage());
            log.error("Error Code:{}", oe.getErrorCode());
            log.error("Request ID:{}", oe.getRequestId());
            log.error("Host ID:{}", oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:{}", ce.getMessage());
        }
        return null;
    }


    @Override
    public String prefixName() {
        return (this.props.isSecret() ? "https://" : "http://") + this.props.getBucketName() + "." + this.props.getEndpoint() + "/";
    }
}
