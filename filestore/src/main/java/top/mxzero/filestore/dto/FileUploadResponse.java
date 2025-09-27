package top.mxzero.filestore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUploadResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fileId;
    private String key;
}