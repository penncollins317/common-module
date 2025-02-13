package top.mxzero.security.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.mxzero.security.core.dto.UserinfoDTO;
import top.mxzero.security.core.dto.UsernamePasswordArgs;
import top.mxzero.security.core.entity.User;
import top.mxzero.security.core.mapper.UserMapper;
import top.mxzero.security.core.service.UserService;

import java.util.List;

/**
 * @author Peng
 * @since 2025/1/21
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserinfoDTO getUserinfo(Long userId) {
        return this.userMapper.findUserinfoById(userId);
    }

    @Override
    public List<UserinfoDTO> getUserinfo(List<Long> userIds) {
        return this.userMapper.findUserinfoByIds(userIds);
    }

    @Override
    public Long addUser(UsernamePasswordArgs args) {
        if (this.userMapper.exists(new QueryWrapper<User>().eq("username", args.getUsername()))) {
            throw new RuntimeException("用户名【" + args.getUsername() + "】已存在");
        }
        User user = new User();
        user.setUsername(args.getUsername());
        user.setPassword(this.passwordEncoder.encode(args.getPassword()));
        user.setNickname(args.getUsername());

        this.userMapper.insert(user);
        return user.getId();
    }
}
