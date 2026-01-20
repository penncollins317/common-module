package top.echovoid.service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.annotations.AuthenticatedRequired;
import top.echovoid.common.dto.RestData;
import top.echovoid.service.user.dto.GetPublicUserinfoRequest;
import top.echovoid.service.user.dto.UserDetailInfoDTO;
import top.echovoid.service.user.dto.UserinfoDTO;
import top.echovoid.service.user.dto.UserinfoModifyDTO;
import top.echovoid.service.user.service.UserService;

import java.security.Principal;
import java.util.List;

/**
 * 用户信息接口
 *
 * @author Penn Collins
 * @since 2025/1/11
 */
@Tag(name = "用户信息接口", description = "提供用户基本信息获取、详细信息获取、参数修改等功能")
@AllArgsConstructor
@RestController
public class UserinfoController {
    private final UserService userService;

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的基本信息")
    @AuthenticatedRequired
    @GetMapping("/api/userinfo")
    public RestData<UserinfoDTO> userinfoApi(@Parameter(hidden = true) Principal principal) {
        return RestData.success(this.userService.getUserinfo(Long.valueOf(principal.getName())));
    }

    /**
     * 获取用户详细信息
     */
    @Operation(summary = "获取用户详细信息", description = "获取当前登录用户的详细个人信息")
    @AuthenticatedRequired
    @GetMapping("/api/userinfo/details")
    public RestData<UserDetailInfoDTO> userDetailInfoApi(@Parameter(hidden = true) Principal principal) {
        return RestData.success(this.userService.getUserDetailInfo(Long.valueOf(principal.getName())));
    }

    /**
     * 修改当前用户信息
     */
    @Operation(summary = "修改当前用户信息", description = "修改当前登录用户的个人信息")
    @AuthenticatedRequired
    @PutMapping("/api/userinfo")
    public RestData<Boolean> updateUserinfoApi(@Parameter(hidden = true) Principal principal,
            @Valid @RequestBody UserinfoModifyDTO dto) {
        dto.setId(Long.valueOf(principal.getName()));
        return RestData.success(this.userService.updateUserinfo(dto));
    }

    /**
     * 批量获取其他用户信息
     */
    @Operation(summary = "批量获取其他用户信息", description = "根据用户ID列表批量获取用户的基本公开信息")
    @PostMapping("/public/userinfo")
    public RestData<List<UserinfoDTO>> publicUserInfoApi(@Valid @RequestBody GetPublicUserinfoRequest requestDTO) {
        return RestData.success(this.userService.getUserinfo(requestDTO.getUserIds()));
    }
}
