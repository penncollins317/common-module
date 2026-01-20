package top.echovoid.filestore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.filestore.service.OssService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * OSS上传签名
 *
 * @author Penn Collins
 * @since 2024/10/20
 */
@Tag(name = "OSS 上传准备接口", description = "提供 OSS 客户端直传所需的签名生成及私有链接获取功能")
@RestController
public class OssPrepareUploadController {
    @Autowired
    private OssService ossService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd");

    /**
     * 上传签名
     *
     * @param name 文件名称
     * @return 签名信息
     */
    @Operation(summary = "获取上传签名", description = "为即将上传的文件生成 OSS 客户端直传所需的 Token 和 Key")
    @GetMapping("/upload/prepare/sign")
    public RestData<UploadSignRecord> ossUploadPrepareApi(
            @Parameter(description = "文件名称（需包含后缀）") @RequestParam("name") String name) {
        // 预生成文件路径
        String datePath = LocalDate.now().format(DATE_TIME_FORMATTER);

        // 生成UUID作为文件名的一部分
        String fileId = UUID.randomUUID().toString().replaceAll("-", "");

        String extension = name.substring(name.lastIndexOf(".") + 1);

        if (extension.equals(name)) {
            throw new ServiceException("文件名缺少后缀");
        }
        // 构建文件名，包含扩展名
        String key = String.format("%s/%s.%s", datePath, fileId, extension);
        String token = ossService.prepareSign(key);
        return RestData.success(new UploadSignRecord(name, key, token, ossService.prefixName() + key));
    }

    public record UploadSignRecord(String filename, String key, String token, String accessUrl) {
    }

    /**
     * 获取私有文件访问链接
     *
     * @param key 文件key
     */
    @Operation(summary = "获取私有访问链接", description = "根据文件 Key 生成带有有效期的私有访问 URL")
    @GetMapping("/oss/private/access")
    public RestData<String> privateAccessUrl(@Parameter(description = "文件在 OSS 中的唯一 Key") @RequestParam String key) {
        return RestData.success(ossService.privateAccessUrl(key));
    }

}
