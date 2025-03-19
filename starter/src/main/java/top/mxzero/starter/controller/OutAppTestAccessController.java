package top.mxzero.starter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.security.core.annotations.AuthenticatedRequired;

import java.security.Principal;

/**
 * @author Peng
 * @since 2025/2/15
 */
@RestController
public class OutAppTestAccessController {

    @PostMapping("/openapi/test")
    public RestData<?> testOpenapiAccessApi(@Autowired Principal principal) {
        return RestData.success(principal.getName());
    }

    @AuthenticatedRequired
    @RequestMapping("/hello")
    public RestData<String> helloApi(Principal principal) {
        return RestData.success(principal.getName());
    }
}
