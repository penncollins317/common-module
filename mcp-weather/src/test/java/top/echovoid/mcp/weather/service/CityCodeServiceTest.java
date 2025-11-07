package top.echovoid.mcp.weather.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.echovoid.mcp.weather.McpWeatherServerApplication;

import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/4/21
 */
@SpringBootTest(classes = McpWeatherServerApplication.class)
class CityCodeServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityCodeServiceTest.class);

    @Autowired
    private CityCodeService cityCodeService;

    @Test
    void queryCityCode() {

        List<String> code = this.cityCodeService.queryCityCode("成都");
        LOGGER.info("code:{}", code);
    }
}