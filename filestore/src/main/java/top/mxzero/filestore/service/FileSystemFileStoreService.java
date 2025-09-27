package top.mxzero.filestore.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.mxzero.common.utils.DeepBeanUtil;
import top.mxzero.common.utils.FileUtils;
import top.mxzero.filestore.dto.FileDownloadResponse;
import top.mxzero.filestore.dto.FileMetadata;
import top.mxzero.filestore.dto.FileUploadRequest;
import top.mxzero.filestore.dto.FileUploadResponse;
import top.mxzero.oss.entity.FileMeta;
import top.mxzero.oss.enums.FileStatus;
import top.mxzero.oss.mapper.FileMetaMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Peng
 * @since 2025/9/26
 */
@Slf4j
@AllArgsConstructor
public class FileSystemFileStoreService implements FileStoreService {
    private final FileSystemProps props;
    private final FileMetaMapper fileMetaMapper;

    @Override
    public FileUploadResponse upload(FileUploadRequest request) throws IOException {
        String extendName = FileUtils.getExtendName(request.getFilename());
        String key = DATE_TIME_FORMATTER.format(LocalDateTime.now())
                + UUID.randomUUID().toString().replaceAll("-", "") + "." + extendName;
        File file = new File(props.getPath() + File.separator + key);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (InputStream inputStream = request.getInputStream(); FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        log.info("File upload:{}", file.getPath());
        FileMeta fileMeta = FileMeta.builder()
                .contentType(request.getContentType())
                .name(request.getFilename())
                .storePath(key)
                .size(request.getSize())
                .status(FileStatus.ACTIVE)
                .isPublic(request.isPublic())
                .userId(request.getUserId())
                .build();
        fileMetaMapper.insert(fileMeta);
        return new FileUploadResponse(fileMeta.getId(), key);
    }

    @Override
    public FileDownloadResponse download(Long fileId) {
        return null;
    }

    @Override
    public boolean delete(Long fileId) {
        return false;
    }

    @Override
    public FileMetadata getMetadata(Long fileId) {
        FileMeta fileMeta = fileMetaMapper.selectById(fileId);
        return DeepBeanUtil.copyProperties(fileMeta, FileMetadata.class);
    }

    @Override
    public String generateAccessUrl(Long fileId, Long expirySeconds) {
        return "";
    }

    @Getter
    @AllArgsConstructor
    @ConfigurationProperties("mxzero.filestore.local")
    public static class FileSystemProps {
        private String path = "filestore_local_files";
    }
}
