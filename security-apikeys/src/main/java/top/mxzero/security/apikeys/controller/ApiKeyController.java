package top.mxzero.security.apikeys.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;

/**
 * @author Peng
 * @since 2025/3/4
 */
@AllArgsConstructor
@RestController
public class ApiKeyController {


    @PostMapping("/api/keys")
    public RestData<String> createApiKey() {
        return RestData.success();
    }
}