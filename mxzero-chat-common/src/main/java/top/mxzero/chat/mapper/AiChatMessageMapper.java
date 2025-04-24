package top.mxzero.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import top.mxzero.chat.entity.AiChatMessage;

/**
 * @author Peng
 * @since 2025/3/18
 */
@MapperScan
public interface AiChatMessageMapper extends BaseMapper<AiChatMessage> {
}
