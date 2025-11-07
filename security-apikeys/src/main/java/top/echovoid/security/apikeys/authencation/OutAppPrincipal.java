package top.echovoid.security.apikeys.authencation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Penn Collins
 * @since 2025/1/20
 */
@Getter
@AllArgsConstructor
public class OutAppPrincipal {
    private final Long appid;
    private final Long timestamp;
    private final String nonce;
    private final String signature;
}
