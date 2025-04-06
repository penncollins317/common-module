package top.mxzero.service.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.mxzero.common.annotations.SearchField;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/1/21
 */
@Data
@TableName("t_user")
public class User {
    @TableId
    private Long id;
    @SearchField
    private String username;
    private String password;
    @SearchField
    private String nickname;
    private String phone;
    private String email;
    private String avatarUrl;
    private Date createdAt;
    private Date updatedAt;
    private Date lastLoginAt;
    @TableField("pwd_version")
    private Long version;
    @TableField("is_active")
    private Boolean active;
}
