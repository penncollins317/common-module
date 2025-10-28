package top.mxzero.starter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mxzero.common.annotations.AuthenticatedRequired;
import top.mxzero.common.annotations.HasScope;
import top.mxzero.common.dto.RestData;

import java.security.Principal;

/**
 * @author Peng
 * @since 2025/2/15
 */
@Slf4j
@RestController
public class OutAppTestAccessController {

    @HasScope("openapi")
    @PostMapping("/openapi/test")
    public RestData<?> testOpenapiAccessApi(@Autowired Principal principal) {
        return RestData.success(principal.getName());
    }

    @AuthenticatedRequired
    @RequestMapping("/hello")
    public RestData<String> helloApi(Principal principal,
                                     @RequestParam("name") String name,
                                     @RequestHeader("X-Name") String headerName,
                                     @CookieValue("c-name") String cName

    ) {
        log.info("headerName={}", headerName);
        return RestData.success(principal.getName());
    }
}
