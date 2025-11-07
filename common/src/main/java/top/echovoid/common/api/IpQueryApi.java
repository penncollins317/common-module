package top.echovoid.common.api;

import lombok.Data;
import org.springframework.web.client.RestTemplate;
import top.echovoid.common.utils.IpUtil;

/**
 * @author Peng
 * @email penncollins317@gmail.com
 * @since 2024/2/12
 */
public class IpQueryApi {

    private final String key;
    private final RestTemplate restTemplate;

    public IpQueryApi(String key, RestTemplate restTemplate) {
        this.key = key;
        this.restTemplate = restTemplate;
    }

    /**
     * @param ip
     */
    public IpQueryResult query(String ip) {
        if (!IpUtil.validateIP(ip)) {
            throw new IllegalStateException("IP Address Not Validated");
        }
        return restTemplate.getForObject("https://restapi.amap.com/v3/ip?key=" + key + "&ip=" + ip, IpQueryResult.class);
    }

    @Data
    static public class IpQueryResult {
        private String status;
        private String info;
        private String infocode;
        private String province;
        private String city;
        private String adcode;
        private String rectangle;

        public boolean isSuccess() {
            return "1".equals(this.status);
        }
    }
}
