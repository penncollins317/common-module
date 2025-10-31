package top.mxzero.filestore.service;

import top.mxzero.common.dto.PageDTO;
import top.mxzero.common.params.PageParam;
import top.mxzero.filestore.dto.FileShareDTO;
import top.mxzero.filestore.dto.FileShareRequestDTO;

/**
 * @author Peng
 * @since 2025/10/30
 */
public interface FileShareService {
    /**
     * 文件分享
     *
     */
    String shareFile(FileShareRequestDTO fileShareDTO);

    /**
     * 检查文件是否可访问
     *
     * @param fileId 文件ID
     * @param token  访问令牌
     */
    boolean checkToken(Long fileId, String token);

    /**
     * 文件共享列表
     *
     * @param userId    用户ID
     * @param pageParam 分页参数
     */
    PageDTO<FileShareDTO> shareFileList(Long userId, PageParam pageParam);
}

