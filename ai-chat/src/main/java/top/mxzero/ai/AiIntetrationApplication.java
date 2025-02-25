package top.mxzero.ai;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
/**
 * @author Peng
 * @since 2024/11/27
 */
@AllArgsConstructor
@EnableWebSocket
@SpringBootApplication
public class AiIntetrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiIntetrationApplication.class, args);
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}