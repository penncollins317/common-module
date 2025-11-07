package top.echovoid.security.apikeys.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.dto.RestData;
import top.echovoid.security.apikeys.dto.request.CreateOutAppRequestDTO;
import top.echovoid.security.apikeys.dto.response.AppInfoDTO;
import top.echovoid.security.apikeys.dto.response.OutAppDTO;
import top.echovoid.security.apikeys.service.OutAppService;

import java.security.Principal;
import java.util.List;

/**
 * 第三方应用接口
 *
 * @author Penn Collins
 * @since 2025/1/20
 */
@AllArgsConstructor
@RestController
public class OutAppController {
    private final OutAppService outAppService;

    /**
     * 创建第三方应用
     */
    @PostMapping("/api/apps")
    public RestData<String> createAppApi(@Valid @RequestBody CreateOutAppRequestDTO app, @Autowired Principal principal) {
        app.setUserId(Long.valueOf(principal.getName()));
        return RestData.success(this.outAppService.createApp(app).toString());
    }

    /**
     * 第三方应用列表
     */
    @GetMapping("/api/apps")
    public RestData<List<OutAppDTO>> listAppApi(@Autowired Principal principal) {
        return RestData.success(this.outAppService.listApp(Long.valueOf(principal.getName())));
    }

    /**
     * 应用详情
     */
    @GetMapping("/api/apps/{appId:\\d+}")
    public RestData<AppInfoDTO> detailAppApi(@PathVariable("appId") Long appId, @Autowired Principal principal) {
        return RestData.success(this.outAppService.getAppInfo(appId, Long.valueOf(principal.getName())));
    }

    /**
     * 重置应用密钥
     */
    @PutMapping("/api/apps/{appId:\\d+}/secret")
    public RestData<String> resetAppSecretApi(@PathVariable("appId") Long appId, @Autowired Principal principal) {
        return RestData.success(this.outAppService.resetSecretKey(appId, Long.valueOf(principal.getName())));
    }

}
