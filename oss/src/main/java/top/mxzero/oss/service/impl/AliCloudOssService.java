package top.mxzero.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.VoidResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import top.mxzero.oss.OssClientType;
import top.mxzero.oss.OssProps;
import top.mxzero.oss.dto.OssUploadResult;
import top.mxzero.oss.service.OssService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
            this.client.putObject(new PutObjectRequest(this.props.getBucketName(), filename, new ByteArrayInputStream(buffer), metadata));
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
        throw new NotImplementedException();
    }

    @Override
    public String prefixName() {
        return (this.props.isSecret() ? "https://" : "http://") + this.props.getEndpoint() + "/";
    }
}
