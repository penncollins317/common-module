package top.mxzero.security.core.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
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
 * @author Peng
 * @since 2025/1/11
 */
@AllArgsConstructor
@RestController
public class UserinfoController {
    private final UserService userService;

    @RequestMapping("/api/userinfo")
    public RestData<UserinfoDTO> userinfoApi(Principal principal) {
        return RestData.success(this.userService.getUserinfo(Long.valueOf(principal.getName())));
    }

    @PostMapping("/public/userinfo")
    public RestData<List<UserinfoDTO>> publicUserInfoApi(@Valid @RequestBody GetPublicUserinfoRequest requestDTO) {
        return RestData.success(this.userService.getUserinfo(requestDTO.getUserIds()));
    }
}
