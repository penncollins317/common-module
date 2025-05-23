package top.mxzero.oss.service.impl;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import top.mxzero.oss.OssClientType;
import top.mxzero.oss.OssProps;
import top.mxzero.oss.dto.OssUploadResult;
import top.mxzero.oss.service.OssService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author zero
 * @email qianmeng6879@163.com
 * @since 2023/8/19
 */
@Slf4j
public class QiNiuYunOssService implements OssService {
    private final OssProps PROPS;
    private final Auth AUTH;
    private final UploadManager UPLOAD_MANAGER;
    private final BucketManager BUCKET_MANAGER;
    private final String prefix;

    public QiNiuYunOssService(OssProps props) {
        AUTH = Auth.create(props.getAccessKey(), props.getSecretKey());
        Configuration CFG = new Configuration(Region.huanan());
        CFG.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        UPLOAD_MANAGER = new UploadManager(CFG);
        BUCKET_MANAGER = new BucketManager(AUTH, CFG);
        PROPS = props;
        this.prefix = (this.PROPS.isSecret() ? "https://" : "http://") + this.PROPS.getEndpoint() + "/";
    }

    private String getToken(String filename) {
        return filename != null ? AUTH.uploadToken(PROPS.getBucketName(), filename) : AUTH.uploadToken(PROPS.getBucketName());
    }


    private OssUploadResult upload(InputStream inputStream, String filename, String token, String contentType, long size) {
        try {
            byte[] data = inputStream.readAllBytes();
            UPLOAD_MANAGER.put(new ByteArrayInputStream(data), filename, token, null, contentType);
            return OssUploadResult.builder()
                    .type(OssClientType.QINIU)
                    .key(filename)
                    .contentType(contentType)
                    .size(data.length)
                    .bucketName(this.PROPS.getBucketName())
                    .url(this.prefixName() + filename)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OssUploadResult upload(byte[] data, String filename, String contentType) {
        return upload(new ByteArrayInputStream(data), filename, getToken(filename), contentType, data.length);
    }


    @Override
    public OssUploadResult upload(InputStream inputStream, String filename, String contentType, long size) {
        return upload(inputStream, filename, getToken(filename), contentType, size);
    }


    @Override
    public boolean exists(String filename) {
        try {
            BUCKET_MANAGER.delete(PROPS.getBucketName(), filename);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean remove(String filename) {
        try {
            BUCKET_MANAGER.stat(PROPS.getBucketName(), filename);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String prepareSign(String name) {
        return AUTH.uploadToken(this.PROPS.getBucketName(), name, 1800, null);
    }

    @Override
    public String privateAccessUrl(String key) {
        return AUTH.privateDownloadUrl(this.prefix + key);
    }

    @Override
    public String prefixName() {
        return prefix;
    }
}
