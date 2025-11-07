package top.echovoid.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.echovoid.chat.entity.AiChatMessage;

/**
 * @author Peng
 * @since 2025/3/18
 */
@Mapper
public interface AiChatMessageMapper extends BaseMapper<AiChatMessage> {
}
