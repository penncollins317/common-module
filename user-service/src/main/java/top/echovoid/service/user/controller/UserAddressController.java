package top.echovoid.service.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * @author Penn Collins
 * @since 2025/5/5
 */
@Tag(name = "用户地址接口", description = "提供用户收货地址的新增、查询、修改、删除等管理功能")
@RestController
@AllArgsConstructor
@AuthenticatedRequired
public class UserAddressController {
    private final UserAddressService userAddressService;

    /**
     * 新增收货地址
     */
    @Operation(summary = "新增收货地址", description = "为当前登录用户创建一个新的收货地址")
    @PostMapping("/user/address")
    public RestData<String> addUserAddressApi(@Valid @RequestBody UserAddressRequestDTO requestDTO,
            @Parameter(hidden = true) Principal principal) {
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
    @Operation(summary = "用户地址列表", description = "获取当前登录用户的所有收货地址列表")
    @GetMapping("/user/address")
    public RestData<List<UserAddressDTO>> listAddressApi(@Parameter(hidden = true) Principal principal) {
        if (principal == null) {
            throw new ServiceException(ServiceErrorCode.USER_NO_AUTH);
        }
        List<UserAddress> result = this.userAddressService
                .list(new QueryWrapper<UserAddress>().eq("user_id", Long.parseLong(principal.getName())));
        return RestData.success(DeepBeanUtil.copyProperties(result, UserAddressDTO::new));
    }

    /**
     * 修改收货地址
     */
    @Operation(summary = "修改收货地址", description = "更新当前登录用户的指定收货地址信息")
    @PutMapping("/user/address/{addressId:\\d+}")
    public RestData<Boolean> editAddressApi(
            @Valid @RequestBody UserAddressRequestDTO requestDTO,
            @Parameter(description = "地址唯一标识ID") @PathVariable("addressId") Long addressId,
            @Parameter(hidden = true) Principal principal) {
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
    @Operation(summary = "删除收货地址", description = "删除指定的收货地址")
    @DeleteMapping("/user/address/{addressId:\\d+}")
    public RestData<Boolean> removeAddressApi(
            @Parameter(description = "地址唯一标识ID") @PathVariable("addressId") Long addressId) {
        return RestData.success(this.userAddressService.removeById(addressId));
    }
}
