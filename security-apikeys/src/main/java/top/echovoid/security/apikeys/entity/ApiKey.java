package top.echovoid.security.apikeys.entity;

import lombok.Data;
import top.echovoid.security.apikeys.enums.ApiKeyStatus;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/3/4
 */
@Data
public class ApiKey {
    private Long id;
    private Long appId;
    private String name;
    private String code;
    private Date createdAt;
    private Date updatedAt;
    private ApiKeyStatus status;
}
