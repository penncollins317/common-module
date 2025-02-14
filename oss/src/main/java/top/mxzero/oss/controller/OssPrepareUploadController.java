package top.mxzero.oss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.oss.OssProps;
import top.mxzero.oss.service.OssService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author Peng
 * @since 2024/10/20
 */
@RestController
public class OssPrepareUploadController {
    @Autowired
    private OssService ossService;
    @Autowired
    private OssProps props;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @GetMapping("/upload/prepare/oss")
    public RestData<String> ossUploadPrepareApi(@RequestParam("name") String name) {
        // 预生成文件路径
        String datePath = LocalDate.now().format(DATE_TIME_FORMATTER);

        // 生成UUID作为文件名的一部分
        String fileId = UUID.randomUUID().toString().replaceAll("-", "");

        String extension = name.substring(name.lastIndexOf(".") + 1);

        // 构建文件名，包含扩展名
        String filePath = String.format("%s/%s.%s", datePath, fileId, extension);
        return RestData.success(ossService.prepareSign(filePath));
    }
}
