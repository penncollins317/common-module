package top.echovoid.filestore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.InputStream;

/**
 * @author Peng
 * @since 2025/9/29
 */
@Getter
@Builder
@AllArgsConstructor
public class FileAccessDTO {
    private String name;
    private String contentType;
    private long size;
    private InputStream inputStream;
}
