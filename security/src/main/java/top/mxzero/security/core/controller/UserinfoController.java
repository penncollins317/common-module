package top.mxzero.security.core.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.security.core.dto.GetPublicUserinfoRequest;
import top.mxzero.security.core.dto.UserinfoDTO;
import top.mxzero.security.core.service.UserService;

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
     * 当前用户信息
     */
    @RequestMapping("/api/userinfo")
    public RestData<UserinfoDTO> userinfoApi(Principal principal) {
        return RestData.success(this.userService.getUserinfo(Long.valueOf(principal.getName())));
    }

    /**
     * 批量获取其他用户信息
     */
    @PostMapping("/public/userinfo")
    public RestData<List<UserinfoDTO>> publicUserInfoApi(@Valid @RequestBody GetPublicUserinfoRequest requestDTO) {
        return RestData.success(this.userService.getUserinfo(requestDTO.getUserIds()));
    }
}
