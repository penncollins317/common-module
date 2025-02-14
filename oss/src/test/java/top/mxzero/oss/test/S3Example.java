package top.mxzero.oss.test;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.nio.file.Paths;

public class S3Example {
    private static String bucketName = "odoo-filestore";
    private static String accessKeyId = "LTAI5tGywaABjLiSwxJRQBqt";
    private static String secretAccessKey = "daIetJnOLvLBb7l7v6vXdKiLY50RkY";
    private static String region = "cn-hangzhou";  // 区域选择
    private static String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";  // 区域选择

    public static void main(String[] args) {
        S3Client s3 = S3Client.builder()
                .region(Region.of(region))  // 区域
                .endpointOverride(URI.create(endpoint))  // 设置 OSS Endpoint
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
        updateObject(s3, "hello.txt");
        listObjects(s3);
    }

    private static void listObjects(S3Client s3) {
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3.listObjectsV2(listObjects);
        response.contents().forEach(s3Object -> System.out.println("Object Key: " + s3Object.key()));
    }

    private static void updateObject(S3Client s3, String key) {
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName)
                .key(key)
                .build();

        PutObjectResponse response = s3.putObject(request, RequestBody.fromContentProvider(ContentStreamProvider.fromByteArray("hello".getBytes()), "text/plain"));
    }
}
