package top.mxzero.common.config;

import java.util.Set;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2023/10/15
 */
public interface ServiceCost {
    /**
     * 用户默认设置密码为空
     */
    String DEFAULT_PASSWORD = "";
    /**
     * 用户名默认前缀
     */
    String USERNAME_DEFAULT_PREFIX = "u_";
    /**
     * 用户默认头像地址
     */
    String USER_DEFAULT_AVATAR_PATH = "/dev/default_avatar.png";
    Long USER_DEFAULT_ROLE_ID = 1L;
    Set<String> USER_DEFAULT_ROLE = Set.of("user");
    String ARTICLE_COMMENT_NUM_TEMPLATE = "article:%s:comment:count";
    String ARTICLE_LIKE_NUM_TEMPLATE = "article:%s:like:count";
    String ARTICLE_browse_USER = "article:%s:browse:user";

    String ARTICLE_BROWSE_TOKEN_KEY = "article:browse:token";
}
