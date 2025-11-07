package top.echovoid.ai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

/**
 * @author Peng
 * @since 2025/3/19
 */
@Slf4j
public class UserTools {
    @Tool(description = "根据用户ID查询用户的爱好")
    public List<String> userHobbyByUserId(Long userId) {
        return List.of("篮球", "乒乓球");
    }

    @Tool(description = "根据用户名查询爱好")
    public List<String> userHobbyByUsername(String username) {
        return List.of("唱歌", "跳舞", "Rapper", "篮球");
    }
}

