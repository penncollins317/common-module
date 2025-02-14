package top.mxzero.oss.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Peng
 * @since 2024/10/20
 */
@Data
public class FileRecordDTO implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String url;
    private String path;
    private String contentType;
    private Long size;
    private String origin;
    private Long userId;
    private Date createdAt;
    private Date updatedAt;
    private String hash;
}
