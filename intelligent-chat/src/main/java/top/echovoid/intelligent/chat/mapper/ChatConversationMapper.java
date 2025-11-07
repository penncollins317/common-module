package top.echovoid.intelligent.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.echovoid.intelligent.chat.entity.ChatConversation;

import java.util.List;

/**
 * @author Peng
 * @since 2025/6/30
 */
@Mapper
public interface ChatConversationMapper extends BaseMapper<ChatConversation> {
    @Select("SELECT conversation_id, user_id, name, created_at, updated_at FROM chat_conversation WHERE user_id = #{userId}")
    List<ChatConversation> findByUserId(@Param("userId") Long userId);
}
