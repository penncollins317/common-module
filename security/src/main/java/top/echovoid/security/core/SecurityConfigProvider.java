package top.echovoid.security.core;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Penn Collins
 * @since 2024/10/13
 */
public interface SecurityConfigProvider {
    /**
     * 需要认证的路径
     */

    default Set<String> ignoreUrls() {
        return Collections.emptySet();
    }



    /**
     * 基于角色认证的路径
     */
    default Map<String, Set<String>> roleBasedUrls() {
        return Collections.emptyMap();
    }
}