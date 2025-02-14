package top.mxzero.oss.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.oss.service.OssService;

/**
 * @author Peng
 * @since 2024/11/30
 */
@AllArgsConstructor
@RestController
public class OssResourceController {
    private final OssService ossService;

    @RequestMapping("/oss/resource")
    public String ossResourceApi(@RequestParam("name") String name) {
        String prepareSign = ossService.prepareSign(name);
        return prepareSign;
    }
}
