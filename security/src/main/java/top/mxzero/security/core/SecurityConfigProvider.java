package top.mxzero.security.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Peng
 * @since 2024/10/13
 */
public interface SecurityConfigProvider {
    /**
     * 需要认证的路径
     */
    default List<String> authorizationUrls() {
        return Collections.emptyList();
    }

    /**
     * 需要角色权限的路径
     */
    default Map<String, List<String>> roleBasedUrls() {
        return Collections.emptyMap();
    }
}