package top.echovoid.security.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Penn Collins
 * @since 2025/2/5
 */
@Data
@Builder
@AllArgsConstructor
public class TokenDTO {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expire_in")
    private long expire;
    @JsonProperty("expire_time")
    private Date expireTime;

    @JsonProperty("refresh_expire_in")
    private Long refreshExpireIn;
    @JsonProperty("refresh_expire_time")
    private Date refreshExpireTime;
}
