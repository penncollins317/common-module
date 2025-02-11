package top.mxzero.security.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.mxzero.security.core.entity.User;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}