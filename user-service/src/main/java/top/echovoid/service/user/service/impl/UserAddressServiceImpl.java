package top.echovoid.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.echovoid.service.user.entity.UserAddress;
import top.echovoid.service.user.mapper.UserAddressMapper;
import top.echovoid.service.user.service.UserAddressService;

/**
 * @author Peng
 * @since 2025/5/5
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {
}