package top.mxzero.security.oauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/clientinfo")
public class ClientInfoController {

    @GetMapping
    public Map<String, Object> clientInfo(@AuthenticationPrincipal Jwt jwt) {
        return Map.of(
                "client_id", jwt.getSubject(),
                "scope", jwt.getClaimAsStringList("scope")
        );
    }
}
