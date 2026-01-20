package top.echovoid.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATETIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
    
    @Bean
    public ObjectMapper objectMapper() {

        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DATETIME)
        );

        return JsonMapper.builder()
                .addModule(timeModule)
                .build();
    }
}
