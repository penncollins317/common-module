package top.echovoid.mcp.weather.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.echovoid.mcp.weather.McpWeatherServerApplication;

/**
 * @author Penn Collins
 * @since 2025/4/21
 */
@SpringBootTest(classes = McpWeatherServerApplication.class)
class WeatherServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherServiceTest.class);
    @Autowired
    private WeatherService weatherService;

    @Test
    void getWeatherForecastByLocation() {
        String result = weatherService.getWeatherForecastByLocation("510100");
        LOGGER.info("温度：{}", result);
    }
}