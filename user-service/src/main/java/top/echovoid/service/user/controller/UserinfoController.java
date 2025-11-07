package top.echovoid.service.user.controller;

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
 * @author Peng
 * @since 2025/1/11
 */
@AllArgsConstructor
@RestController
public class UserinfoController {
    private final UserService userService;

    /**
     * 获取用户信息
     */
    @AuthenticatedRequired
    @RequestMapping("/api/userinfo")
    public RestData<UserinfoDTO> userinfoApi(Principal principal) {
        return RestData.success(this.userService.getUserinfo(Long.valueOf(principal.getName())));
    }

    /**
     * 获取用户详细信息
     */
    @AuthenticatedRequired
    @RequestMapping("/api/userinfo/details")
    public RestData<UserDetailInfoDTO> userDetailInfoApi(Principal principal) {
        return RestData.success(this.userService.getUserDetailInfo(Long.valueOf(principal.getName())));
    }

    /**
     * 修改当前用户信息
     */
    @AuthenticatedRequired
    @PutMapping("/api/userinfo")
    public RestData<Boolean> updateUserinfoApi(Principal principal, @Valid @RequestBody UserinfoModifyDTO dto) {
        dto.setId(Long.valueOf(principal.getName()));
        return RestData.success(this.userService.updateUserinfo(dto));
    }


    /**
     * 批量获取其他用户信息
     */
    @PostMapping("/public/userinfo")
    public RestData<List<UserinfoDTO>> publicUserInfoApi(@Valid @RequestBody GetPublicUserinfoRequest requestDTO) {
        return RestData.success(this.userService.getUserinfo(requestDTO.getUserIds()));
    }
}
