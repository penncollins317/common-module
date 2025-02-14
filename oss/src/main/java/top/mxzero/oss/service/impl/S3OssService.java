package top.mxzero.oss.service.impl;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import top.mxzero.oss.OssClientType;
import top.mxzero.oss.OssProps;
import top.mxzero.oss.dto.OssUploadResult;
import top.mxzero.oss.service.OssService;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;

/**
 * @author Peng
 * @since 2024/11/29
 */
@Slf4j
public class S3OssService implements OssService {
    private final S3Client client;
    private final S3Presigner presigner;
    private final OssProps props;

    public S3OssService(OssProps props) {
        this.props = props;
        this.client = S3Client.builder()
                .region(Region.of(props.getRegion()))
                .endpointOverride(URI.create(props.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())))
                .build();

        this.presigner = S3Presigner.builder()
                .region(Region.of(props.getRegion()))
                .endpointOverride(URI.create(props.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey())))
                .build();
    }

    @Override
    public OssUploadResult upload(byte[] data, String filename, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(this.props.getBucketName())
                .key(filename)
                .build();

        this.client.putObject(request, RequestBody.fromContentProvider(ContentStreamProvider.fromByteArray(data), contentType));
        return OssUploadResult.builder()
                .type(OssClientType.S3)
                .key(filename)
                .contentType(contentType)
                .size(data.length)
                .bucketName(this.props.getBucketName())
                .url(this.prefixName() + filename)
                .build();
    }

    @Override
    public OssUploadResult upload(InputStream inputStream, String filename, String contentType, long size) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(this.props.getBucketName())
                .key(filename)
                .build();

        this.client.putObject(request, RequestBody.fromContentProvider(ContentStreamProvider.fromInputStream(inputStream), contentType));
        return OssUploadResult.builder()
                .type(OssClientType.S3)
                .key(filename)
                .contentType(contentType)
                .size(size)
                .bucketName(this.props.getBucketName())
                .url(this.prefixName() + filename)
                .build();
    }

    @Override
    public boolean exists(String filename) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(this.props.getBucketName())
                    .key(filename)
                    .build();
            this.client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }

    @Override
    public boolean remove(String filename) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(this.props.getBucketName())
                    .key(filename)
                    .build();
            this.client.deleteObject(deleteObjectRequest);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public String prepareSign(String name) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(this.props.getBucketName())
                    .key(name)
                    .build();
            Duration expiration = Duration.ofHours(1);
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(p -> p.putObjectRequest(putObjectRequest)
                    .signatureDuration(expiration));
            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String prefixName() {
        return this.props.getEndpoint() + "/" + this.props.getBucketName() + "/";
    }
}
