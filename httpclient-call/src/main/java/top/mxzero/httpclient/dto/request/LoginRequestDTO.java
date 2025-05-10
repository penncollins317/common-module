package top.mxzero.httpclient.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Peng
 * @since 2025/5/9
 */
@Data
@AllArgsConstructor
public class LoginRequestDTO {
    private String username;
    private String password;
}
