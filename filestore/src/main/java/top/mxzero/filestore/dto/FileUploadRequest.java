package top.mxzero.filestore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

/**
 * @author Peng
 * @since 2025/9/26
 */
@Data
@Builder
@AllArgsConstructor
public class FileUploadRequest {
    private String filename;
    private InputStream inputStream;
    private long size;
    private String contentType;
    private boolean isPublic;
    private Long userId;
}