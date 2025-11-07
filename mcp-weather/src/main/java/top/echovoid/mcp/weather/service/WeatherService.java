package top.echovoid.mcp.weather.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import top.echovoid.mcp.weather.config.AMAPConfigProperties;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final AMAPConfigProperties amapConfigProperties;
    private static final String BASE_URL = "https://restapi.amap.com";
    private final RestClient restClient;

    public WeatherService(AMAPConfigProperties amapConfigProperties) {
        this.amapConfigProperties = amapConfigProperties;
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Props(
            String status,
            String count,
            String info,
            String infocode,
            List<Period> lives
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Period(
            String province,
            String city,
            String adcode,
            String weather,
            String temperature,
            String winddirection,
            String windpower,
            String humidity,
            String reporttime,
            @JsonProperty("temperature_float")
            String reportemperatureFloatttime,
            @JsonProperty("humidity_float")
            String humidityFloat
    ) {
    }


    /**
     * Get forecast for a specific latitude/longitude
     *
     * @param cityCode 城市地区编码
     * @return 城市地区编码
     * @throws RestClientException if the request fails
     */
    @Tool(description = "根据城市地区编码查询天气情况")
    public String getWeatherForecastByLocation(String cityCode) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/v3/weather/weatherInfo")
                .queryParam("key", amapConfigProperties.getWebServiceKey())
                .queryParam("city", cityCode);
        Props result = restClient.get().uri(uriComponentsBuilder.build().toUriString()).retrieve().body(Props.class);
        return result.lives().stream().map(p -> String.format("""
                %s:
                温度: %s°
                风向: %s %s级
                """, p.city(), p.temperature(), p.winddirection(), p.windpower())).collect(Collectors.joining());
    }
}