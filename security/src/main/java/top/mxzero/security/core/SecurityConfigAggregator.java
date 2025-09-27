package top.mxzero.security.core;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证配置聚合器
 *
 * @author Peng
 * @since 2024/10/14
 */
public class SecurityConfigAggregator {
    private final List<SecurityConfigProvider> securityConfigProviders;

    public SecurityConfigAggregator(List<SecurityConfigProvider> securityConfigProviders) {
        this.securityConfigProviders = securityConfigProviders;
    }

    public Set<String> getIgnoreUrls() {
        return this.securityConfigProviders.stream()
                .map(SecurityConfigProvider::ignoreUrls)
                .flatMap(Set::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }


    // 获取基于角色的路径
    public Map<String, Set<String>> getRoleBasedUrls() {
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

