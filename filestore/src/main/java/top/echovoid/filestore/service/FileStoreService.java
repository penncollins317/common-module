package top.echovoid.filestore.service;

import jakarta.annotation.Nullable;
import top.echovoid.common.dto.PageDTO;
import top.echovoid.common.params.PageParam;
import top.echovoid.filestore.dto.FileAccessDTO;
import top.echovoid.filestore.dto.FileDownloadResponse;
import top.echovoid.filestore.dto.FileUploadRequest;
import top.echovoid.filestore.dto.FileUploadResponse;
import top.echovoid.filestore.dto.FileMetaDTO;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Penn Collins
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
     * 检查指定文件是否可访问
     *
     * @param fileId 文件ID
     * @param userId 用户ID
     * @param token  文件访问凭证
     */
    boolean checkAccessible(Long fileId, @Nullable Long userId, @Nullable String token);

    /**
     * 检查指定文件是否可访问
     *
     * @param fileKey 文件Key
     * @param userId  用户ID
     * @param token   文件访问凭证
     */
    boolean checkAccessible(String fileKey, @Nullable Long userId, @Nullable String token);

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
    FileMetaDTO getMetadata(Long fileId);

    /**
     * 生成可访问的文件URL（支持临时授权）
     *
     * @param fileId        文件唯一标识
     * @param expirySeconds 链接有效期（秒），如果为null或0则为永久链接（仅在实现支持时生效）
     * @return 文件访问URL
     */
    String generateAccessUrl(Long fileId, Long expirySeconds);

    /**
     * 获取文件访问流
     *
     * @param key 文件存储key
     */
    Optional<FileAccessDTO> getInputStreamByKey(String key);


    /**
     * 文件列表
     *
     * @param userId 用户ID
     */
    PageDTO<FileMetaDTO> fileList(Long userId, PageParam pageParam);

}