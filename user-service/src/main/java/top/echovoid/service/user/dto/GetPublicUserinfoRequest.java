package top.echovoid.service.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/2/13
 */
@Data
public class GetPublicUserinfoRequest {
    @NotNull
    @Size(min = 1, max = 100)
    private List<Long> userIds;
}
