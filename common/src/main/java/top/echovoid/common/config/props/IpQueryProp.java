package top.echovoid.common.config.props;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author Penn Collins
 * @since 2024/10/3
 */
@Data
@ConditionalOnProperty("echovoid.api.ip.key")
public class IpQueryProp {
    private String key;
}
