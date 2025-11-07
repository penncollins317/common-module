package top.echovoid.httpclient.dto.response;

import lombok.Data;

/**
 * @author Peng
 * @since 2025/1/21
 */
@Data
public class UserinfoDTO {
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
}