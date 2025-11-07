package top.echovoid.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.echovoid.common.dto.PageDTO;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.common.params.PageExtendParam;
import top.echovoid.common.utils.DeepBeanUtil;
import top.echovoid.service.user.dto.*;
import top.echovoid.service.user.entity.User;
import top.echovoid.service.user.entity.UserAccount;
import top.echovoid.service.user.mapper.RoleMapper;
import top.echovoid.service.user.mapper.UserAccountMapper;
import top.echovoid.service.user.mapper.UserMapper;
import top.echovoid.service.user.service.UserService;

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
    private final RoleMapper roleMapper;
    private final UserAccountMapper accountMapper;

    @Override
    public UserinfoDTO getUserinfo(Long userId) {
        return this.userMapper.findUserinfoById(userId);
    }

    @Override
    public UserinfoDTO getUserinfoByUsername(String username) {
        return this.userMapper.findUserinfoByUsername(username);
    }

    @Nullable
    @Override
    public UserDetailInfoDTO getUserDetailInfo(Long userId) {
        User user = this.userMapper.selectById(userId);
        UserDetailInfoDTO userDetailInfoDTO = DeepBeanUtil.copyProperties(user, UserDetailInfoDTO.class);
        List<UserAccount> accountList = this.accountMapper.selectList(new QueryWrapper<UserAccount>().eq("user_id", user.getId()));
        userDetailInfoDTO.setLoginAccountList(accountList);
        return userDetailInfoDTO;
    }

    @Override
    public List<UserinfoDTO> getUserinfo(List<Long> userIds) {
        return this.userMapper.findUserinfoByIds(userIds);
    }

    @Override
    public PageDTO<UserinfoDTO> search(PageExtendParam parma) {
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

    @Override
    @Transactional
    public boolean BindAccountDTO(BindAccountDTO dto) {
        UserAccount account = this.accountMapper.selectOne(new QueryWrapper<UserAccount>().eq("account_type", dto.getAccountType()).eq("account_value", dto.getAccountValue()));
        if (account != null) {
            if (account.getUserId().equals(dto.getUserId())) {
                throw new ServiceException("用户已绑定该账号");
            } else {
                throw new ServiceException("该账号已被其他用户绑定");
            }
        }

        account = DeepBeanUtil.copyProperties(dto, UserAccount::new);
        account.setValidated(false);
        return this.accountMapper.insert(account) > 0;
    }

    @Override
    public List<String> getUserRolesByUserId(Long userId) {
        return this.roleMapper.findNameByUserId(userId);
    }
}
