package top.mxzero.httpclient.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Peng
 * @since 2025/5/9
 */
@Data
public class LoginResponseDTO {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expire_in")
    private long expire;
    @JsonProperty("expire_time")
    private String expireTime;

    @JsonProperty("refresh_expire_in")
    private Long refreshExpireIn;
    @JsonProperty("refresh_expire_time")
    private String refreshExpireTime;
}
