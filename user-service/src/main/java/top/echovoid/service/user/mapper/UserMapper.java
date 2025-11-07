package top.echovoid.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.echovoid.service.user.dto.UserinfoDTO;
import top.echovoid.service.user.entity.User;

import java.util.List;

/**
 * @author Peng
 * @since 2025/2/5
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    UserinfoDTO findUserinfoById(Long userId);
    UserinfoDTO findUserinfoByUsername(String username);

    List<UserinfoDTO> findUserinfoByIds(@Param("userIds") List<Long> userIds);
}