package top.mxzero.filestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.oss.service.OssService;

/**
 * 私有文件访问接口
 *
 * @author Peng
 * @since 2025/5/24
 */
@RestController
public class FileAccessController {
    @Autowired
    private OssService ossService;

    /**
     * 获取私有访问链接
     *
     * @param filename 文件路径
     */
    @RequestMapping("/filestore/private")
    public RestData<String> filestorePrivateAccessApi(@RequestParam("file") String filename) {
        return RestData.ok(ossService.privateAccessUrl(filename));
    }
}


