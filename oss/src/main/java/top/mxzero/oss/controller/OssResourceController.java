package top.mxzero.oss.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.oss.service.OssService;

/**
 * @author Peng
 * @since 2024/11/30
 */
@AllArgsConstructor
@RestController
public class OssResourceController {
    private final OssService ossService;

    /**
     * 对象存储上传凭证
     *
     * @param name 文件名称
     */
    @RequestMapping("/api/oss/resource")
    public RestData<String> ossResourceApi(@RequestParam("name") String name) {
        return RestData.success(ossService.prepareSign(name));
    }
}
