package top.mxzero.service.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * @author Peng
 * @since 2025/2/13
 */
@Data
public class GetPublicUserinfoRequest {
    @NotNull
    @Size(min = 1, max = 100)
    private List<Long> userIds;
}
