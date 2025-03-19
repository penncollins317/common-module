package top.mxzero.security.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import top.mxzero.security.core.enums.OAuth2Provider;

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
    private OAuth2Provider provider;
    private Date createdAt;
    private Date updatedAt;
}