package top.echovoid.filestore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
 * @author Penn Collins
 * @since 2025/10/30
 */
@Data
public class FileShareRequestDTO {
    @NotBlank
    private String fileId;
    @JsonIgnore
    private Long userId;
    private Date expireAt;
}