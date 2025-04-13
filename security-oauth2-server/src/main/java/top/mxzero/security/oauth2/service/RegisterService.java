package top.mxzero.security.oauth2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.security.oauth2.dto.RegisterDTO;
import top.mxzero.service.user.entity.User;
import top.mxzero.service.user.mapper.UserMapper;

/**
 * @author Peng
 * @since 2025/4/12
 */
@Service
@AllArgsConstructor
public class RegisterService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户邮箱注册
     *
     * @param dto 注册信息
     * @return 用户ID
     */
    @Transactional
    public Long registerUser(RegisterDTO dto) {
        if (this.userMapper.exists(new QueryWrapper<User>().eq("email", dto.getEmail()))) {
            throw new ServiceException("邮箱已存在");
        }
        if (this.userMapper.exists(new QueryWrapper<User>().eq("username", dto.getUsername()))) {
            throw new ServiceException("用户名已存在");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        this.userMapper.insert(user);
        return user.getId();
    }
}
