package top.echovoid.starter.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Penn Collins
 * @since 2025/11/11
 */
@RestController
public class CurrentClientController {
    @RequestMapping("/ip")
    public String ip(HttpServletRequest request) {
        return request.getRemoteHost();
    }
}

