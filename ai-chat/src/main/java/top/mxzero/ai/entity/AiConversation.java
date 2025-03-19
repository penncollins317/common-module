package top.mxzero.ai.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Data
public class AiConversation {
    @TableId
    private String conversationId;
    private String title;
    private Long userId;
    private Date createdAt;
    private Date updatedAt;
    @TableLogic
    private Boolean deleted;
}