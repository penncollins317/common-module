package top.echovoid.security.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.echovoid.service.user.entity.Role;
import top.echovoid.service.user.entity.User;
import top.echovoid.service.user.mapper.RoleMapper;
import top.echovoid.service.user.mapper.UserMapper;

import java.util.List;

/**
 * @author Peng
 * @since 2025/1/21
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException("账号【" + username + "】不存在");
        }
        List<Role> roles = this.roleMapper.findByUserId(user.getId());
        List<SimpleGrantedAuthority> authorityList = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()), user.getPassword(), true, true, StringUtils.hasLength(user.getPassword()), user.getActive(), authorityList
        );
    }
}
