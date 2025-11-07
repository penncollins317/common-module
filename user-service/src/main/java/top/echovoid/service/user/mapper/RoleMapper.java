package top.echovoid.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.echovoid.service.user.entity.Role;

import java.util.List;

/**
 * @author Penn Collins
 * @since 2025/2/5
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> findByUserId(Long userId);
    List<String> findNameByUserId(Long userId);
}