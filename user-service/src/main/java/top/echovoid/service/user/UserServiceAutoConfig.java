package top.echovoid.service.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import top.echovoid.service.user.mapper.RoleMapper;
import top.echovoid.service.user.mapper.UserMapper;
import top.echovoid.service.user.service.UserDetailsServiceImpl;

/**
 * @author Penn Collins
 * @since 2025/4/4
 */
@MapperScan("top.echovoid.service.user.mapper")
@Configuration
@ComponentScan
public class UserServiceAutoConfig {
    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper, RoleMapper roleMapper) {
        return new UserDetailsServiceImpl(userMapper, roleMapper);
    }
}
