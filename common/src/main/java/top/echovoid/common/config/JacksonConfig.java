package top.echovoid.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final DateTimeFormatter DATETIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public ObjectMapper objectMapper() {

        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DATETIME)
        );
        timeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DATETIME)
        );

        return JsonMapper.builder()
                .addModule(timeModule)
                .build();
    }
}
