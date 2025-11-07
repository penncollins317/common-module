package top.echovoid.chat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Penn Collins
 * @since 2024/12/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiChatMessage {
    @TableId
    private Long id;
    private String conversationId;
    private String content;
    private String role;
    private Long timestamp;
    private Date createdAt;
}