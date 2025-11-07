package top.echovoid.oss.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Penn Collins
 * @since 2024/10/20
 */
@Data
@Builder
public class PrepareUploadResult implements Serializable {
    /**
     * 预上传文件路径
     */
    private String path;
    private String hash;
    private STATE state;
    /**
     * 当state == CACHE时返回，表示文件hash值存在，返回该文件的访问url
     */
    private String url;

    /**
     * 当 state == CREATE时返回，表示文件上传ID
     */
    private String fileId;

    /**
     * 结果类型
     * CACHE:存在文件hash，无需上传
     * CREATE:文件不存在，需要执行上传
     */
    public static enum STATE {
        CACHE,
        CREATE
    }
}
