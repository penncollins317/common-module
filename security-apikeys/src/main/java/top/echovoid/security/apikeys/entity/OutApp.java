package top.echovoid.security.apikeys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import top.echovoid.security.apikeys.enums.OutAppStatus;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/1/20
 */
@Data
public class OutApp {
    @TableId
    private Long id;
    private String name;
    private String secretKey;
    private String aesKey;
    private String iconUrl;
    private String description;
    private String devServerUrl;
    private Long userId;
    private OutAppStatus status;
    private Date createdAt;
    private Date updatedAt;
}
