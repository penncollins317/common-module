package top.echovoid.oss.service.impl;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import top.echovoid.oss.OssClientType;
import top.echovoid.oss.OssProps;
import top.echovoid.oss.dto.OssUploadResult;
import top.echovoid.oss.service.OssService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Penn Collins
 * @since 2024/9/21
 */
@Slf4j
public class MinioOssService implements OssService {
    private final OssProps props;
    private final MinioClient client;

    public MinioOssService(OssProps props) {
        this.props = props;
        this.client = MinioClient.builder().endpoint(props.isSecret() ? "https://" : "http://" + props.getEndpoint()).credentials(props.getAccessKey(), props.getSecretKey()).build();
    }

    @Override
    public OssUploadResult upload(byte[] data, String filename, String contentType) {
        PutObjectArgs args = PutObjectArgs.builder().bucket(this.props.getBucketName()).stream(new ByteArrayInputStream(data), data.length, -1).contentType(contentType).object(filename).build();
        try {
            this.client.putObject(args);
            return OssUploadResult.builder()
                    .type(OssClientType.MINIO)
                    .key(filename)
                    .contentType(contentType)
                    .size(data.length)
                    .bucketName(this.props.getBucketName())
                    .url(this.prefixName() + filename)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OssUploadResult upload(InputStream inputStream, String filename, String contentType, long size) {
        try {
            return this.upload(inputStream.readAllBytes(), filename, contentType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String filename) {
        try {
            this.client.statObject(StatObjectArgs.builder().bucket(this.props.getBucketName()).object(filename).build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean remove(String filename) {
        try {
            this.client.removeObject(RemoveObjectArgs.builder().bucket(this.props.getBucketName()).object(filename).build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long removeBatch(List<String> objectNames) {
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(this.props.getBucketName())
                .objects(objectNames.stream().map(DeleteObject::new).toList())
                .build();
        try {
            client.removeObjects(removeObjectsArgs);
        } catch (Exception ignored) {
        }

        return objectNames.size();
    }

    @Override
    public String prepareSign(String name) {
        try {
            return client.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(props.getBucketName())
                            .object(name)
                            .method(Method.PUT)
                            .expiry(5, TimeUnit.MINUTES)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String privateAccessUrl(String key) {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .expiry(3600, TimeUnit.SECONDS)
                .bucket(props.getBucketName())
                .object(key)
                .method(Method.GET)
                .build();
        try {
            return client.getPresignedObjectUrl(args);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public String prefixName() {
        return (this.props.isSecret() ? "https://" : "http://") + this.props.getEndpoint() + "/" + props.getBucketName() + "/";
    }
}
