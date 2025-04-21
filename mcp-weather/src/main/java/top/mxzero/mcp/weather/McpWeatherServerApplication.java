package top.mxzero.mcp.weather;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.mxzero.mcp.weather.config.AMAPConfigProperties;
import top.mxzero.mcp.weather.service.CityCodeService;
import top.mxzero.mcp.weather.service.WeatherService;

/**
 * @author Peng
 * @since 2025/4/19
 */
@EnableConfigurationProperties(AMAPConfigProperties.class)
@MapperScan("top.mxzero.mcp.weather.mapper")
@SpringBootApplication
public class McpWeatherServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpWeatherServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }

    @Bean
    public ToolCallbackProvider cityCodeTools(CityCodeService cityCodeService) {
        return MethodToolCallbackProvider.builder().toolObjects(cityCodeService).build();
    }
}
