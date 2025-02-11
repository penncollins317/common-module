package top.mxzero.security.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.mxzero.security.core.entity.Role;

import java.util.List;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> findByUserId(Long userId);
}