package top.mxzero.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.common.dto.PageDTO;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.common.params.PageSearchParam;
import top.mxzero.common.utils.DeepBeanUtil;
import top.mxzero.service.user.dto.UserinfoDTO;
import top.mxzero.service.user.dto.UserinfoModifyDTO;
import top.mxzero.service.user.dto.UsernamePasswordArgs;
import top.mxzero.service.user.entity.User;
import top.mxzero.service.user.mapper.UserMapper;
import top.mxzero.service.user.service.UserService;

import java.util.List;
import java.util.Set;

/**
 * @author Peng
 * @since 2025/1/21
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public UserinfoDTO getUserinfo(Long userId) {
        return this.userMapper.findUserinfoById(userId);
    }

    @Override
    public UserinfoDTO getUserinfoByUsername(String username) {
        return this.userMapper.findUserinfoByUsername(username);
    }

    @Override
    public List<UserinfoDTO> getUserinfo(List<Long> userIds) {
        return this.userMapper.findUserinfoByIds(userIds);
    }

    @Override
    public PageDTO<UserinfoDTO> search(PageSearchParam parma) {
        parma.setFields(Set.of("id", "avatar_url", "username", "nickname", "pwd_version"));
        return PageDTO.<User, UserinfoDTO>wrap(this.userMapper, User.class, parma, UserinfoDTO::new);
    }

    @Override
    @Transactional
    public Long addUser(UsernamePasswordArgs args) {
        if (this.userMapper.exists(new QueryWrapper<User>().eq("username", args.getUsername()))) {
            throw new ServiceException("用户名【" + args.getUsername() + "】已存在");
        }
        User user = new User();
        user.setUsername(args.getUsername());
        user.setPassword(args.getPassword());
        user.setNickname(args.getUsername());

        this.userMapper.insert(user);
        return user.getId();
    }

    @Override
    @Transactional
    public boolean updateUserinfo(UserinfoModifyDTO dto) {
        User user = DeepBeanUtil.copyProperties(dto, User::new);
        return this.userMapper.updateById(user) > 0;
    }
}
