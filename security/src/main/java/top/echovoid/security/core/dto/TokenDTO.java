package top.echovoid.security.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Penn Collins
 * @since 2025/2/5
 */
@Data
@Builder
@AllArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private long expireIn;
    private long refreshExpireIn;
}
