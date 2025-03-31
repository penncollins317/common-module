package top.mxzero.security.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/3/19
 */
@Data
@TableName("oauth2_user_related")
public class OAuth2UserRelated {
    @TableId
    private Long id;
    private Long userId;
    private String outUid;
    private String provider;
    private Date createdAt;
    private Date updatedAt;
}