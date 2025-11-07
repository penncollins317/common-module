package top.echovoid.filestore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import top.echovoid.oss.dto.FileMetaDTO;

import java.time.LocalDateTime;

/**
 * @author Peng
 * @since 2025/11/1
 */
@Getter
@Builder
@AllArgsConstructor
public class FileShareDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private FileMetaDTO fileMetaDTO;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expireAt;
    private String token;
    private String accessUrl;
}


