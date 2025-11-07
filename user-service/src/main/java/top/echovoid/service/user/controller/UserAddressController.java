package top.echovoid.service.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.annotations.AuthenticatedRequired;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.exceptions.ServiceErrorCode;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.common.utils.DeepBeanUtil;
import top.echovoid.service.user.dto.UserAddressDTO;
import top.echovoid.service.user.dto.UserAddressRequestDTO;
import top.echovoid.service.user.entity.UserAddress;
import top.echovoid.service.user.service.UserAddressService;

import java.security.Principal;
import java.util.List;


/**
 * 用户地址接口
 *
 * @author Peng
 * @since 2025/5/5
 */
@RestController
@AllArgsConstructor
@AuthenticatedRequired
public class UserAddressController {
    private final UserAddressService userAddressService;

    /**
     * 新增收货地址
     */
    @PostMapping("/user/address")
    public RestData<String> addUserAddressApi(@Valid @RequestBody UserAddressRequestDTO requestDTO, Principal principal) {
        if (principal == null) {
            throw new ServiceException(ServiceErrorCode.USER_NO_AUTH);
        }
        UserAddress userAddress = DeepBeanUtil.copyProperties(requestDTO, UserAddress::new);
        userAddress.setUserId(Long.parseLong(principal.getName()));
        this.userAddressService.save(userAddress);
        return RestData.success(userAddress.getId().toString());
    }

    /**
     * 用户地址列表
     */
    @GetMapping("/user/address")
    public RestData<List<UserAddressDTO>> listAddressApi(Principal principal) {
        if (principal == null) {
            throw new ServiceException(ServiceErrorCode.USER_NO_AUTH);
        }
        List<UserAddress> result = this.userAddressService.list(new QueryWrapper<UserAddress>().eq("user_id", Long.parseLong(principal.getName())));
        return RestData.success(DeepBeanUtil.copyProperties(result, UserAddressDTO::new));
    }

    /**
     * 修改收货地址
     */
    @PutMapping("/user/address/{addressId:\\d+}")
    public RestData<Boolean> editAddressApi(@Valid @RequestBody UserAddressRequestDTO requestDTO, @PathVariable("addressId") Long addressId, Principal principal) {
        if (principal == null) {
            throw new ServiceException(ServiceErrorCode.USER_NO_AUTH);
        }
        UserAddress userAddress = DeepBeanUtil.copyProperties(requestDTO, UserAddress::new);
        userAddress.setId(addressId);
        userAddress.setUserId(Long.parseLong(principal.getName()));
        return RestData.success(this.userAddressService.updateById(userAddress));
    }

    /**
     * 删除收货地址
     */
    @DeleteMapping("/user/address/{addressId:\\d+}")
    public RestData<Boolean> removeAddressApi(@PathVariable("addressId") Long addressId) {
        return RestData.success(this.userAddressService.removeById(addressId));
    }
}
