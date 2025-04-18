package top.mxzero.security.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证配置聚合器
 *
 * @author Peng
 * @since 2024/10/14
 */
public final class SecurityConfigAggregator {
    private final List<SecurityConfigProvider> securityConfigProviders;

    public SecurityConfigAggregator(List<SecurityConfigProvider> securityConfigProviders) {
        this.securityConfigProviders = securityConfigProviders;
    }

    // 获取需要认证的路径
    public String[] getAuthorizationUrls() {
        return securityConfigProviders.stream()
                .map(SecurityConfigProvider::authorizationUrls)
                .flatMap(List::stream)
                .distinct()
                .toArray(String[]::new);
    }

    // 获取基于角色的路径
    public Map<String, List<String>> getRoleBasedUrls() {
        return securityConfigProviders.stream()
                .map(SecurityConfigProvider::roleBasedUrls)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> {
                            existing.addAll(replacement);
                            return existing;
                        }
                ));
    }
}

