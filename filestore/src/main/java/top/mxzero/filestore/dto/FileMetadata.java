package top.mxzero.filestore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FileMetadata {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String storePath;
    private String name;
    private Long size;
    private String contentType;
    private Boolean isPublic;
    private Date createdAt;
    private Date updatedAt;
}