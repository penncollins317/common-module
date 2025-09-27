package top.mxzero.filestore.service;

import top.mxzero.filestore.dto.FileDownloadResponse;
import top.mxzero.filestore.dto.FileMetadata;
import top.mxzero.filestore.dto.FileUploadRequest;
import top.mxzero.filestore.dto.FileUploadResponse;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * @author Peng
 * @since 2025/9/26
 */
public interface FileStoreService {
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd");

    /**
     * 上传文件
     *
     * @param request 上传请求对象
     * @return 上传结果（包含文件ID、访问URL等）
     */
    FileUploadResponse upload(FileUploadRequest request) throws IOException;

    /**
     * 下载文件
     *
     * @param fileId 文件唯一标识
     * @return 文件下载结果（包含字节流或输入流等信息）
     */
    FileDownloadResponse download(Long fileId);

    /**
     * 删除文件
     *
     * @param fileId 文件唯一标识
     * @return 删除是否成功
     */
    boolean delete(Long fileId);

    /**
     * 获取文件元信息
     *
     * @param fileId 文件唯一标识
     * @return 文件元信息（大小、类型、权限等）
     */
    FileMetadata getMetadata(Long fileId);

    /**
     * 生成可访问的文件URL（支持临时授权）
     *
     * @param fileId        文件唯一标识
     * @param expirySeconds 链接有效期（秒），如果为null或0则为永久链接（仅在实现支持时生效）
     * @return 文件访问URL
     */
    String generateAccessUrl(Long fileId, Long expirySeconds);
}

