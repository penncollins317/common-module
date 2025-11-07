package top.echovoid.intelligent.chat.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author Penn Collins
 * @since 2025/6/30
 */
@Data
public class ChatConversation {
    @TableId
    private String conversationId;
    private Long userId;
    private String name;
    private Integer msgCnt;
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}
