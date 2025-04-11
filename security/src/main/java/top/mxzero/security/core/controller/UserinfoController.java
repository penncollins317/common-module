package top.mxzero.security.core.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.mxzero.common.dto.RestData;
import top.mxzero.security.core.annotations.AuthenticatedRequired;
import top.mxzero.security.core.dto.GetPublicUserinfoRequest;
import top.mxzero.service.user.dto.UserinfoDTO;
import top.mxzero.service.user.dto.UserinfoModifyDTO;
import top.mxzero.service.user.service.UserService;

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
