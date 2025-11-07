package top.echovoid.ai.dto.response;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {
    private String conversationId;
    private String title;
    private Long userId;
    private Date createdAt;
    private Date updatedAt;
}
