package top.echovoid.filestore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.filestore.dto.FileAccessDTO;
import top.echovoid.filestore.service.FileStoreService;
import top.echovoid.filestore.service.OssService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Optional;

/**
 * 文件访问接口
 *
 * @author Penn Collins
 * @since 2025/5/24
 */
@Tag(name = "文件访问接口", description = "提供私有文件访问链接生成及文件流直接访问功能")
@Slf4j
@RestController
@AllArgsConstructor
public class FileAccessController {
    private final OssService ossService;
    private final FileStoreService fileStoreService;

    /**
     * 获取私有访问链接
     *
     * @param filename 文件路径
     */
    @Operation(summary = "获取私有访问链接", description = "生成带有签名的过期访问链接，用于私有存储中的文件访问")
    @GetMapping("/filestore/private")
    public RestData<String> filestorePrivateAccessApi(
            @Parameter(description = "文件路径/Key") @RequestParam("file") String filename) {
        return RestData.ok(ossService.privateAccessUrl(filename));
    }

    /**
     * 文件流访问
     *
     * @param path 文件路径
     */
    @Operation(summary = "文件流访问", description = "直接通过 HTTP 通道读取并返回文件流，支持权限验证")
    @GetMapping("/filestore/access/{*path}")
    public ResponseEntity<InputStreamResource> fileAccessApi(
            @Parameter(description = "文件相对路径") @PathVariable String path,
            @Parameter(hidden = true) Principal principal,
            @Parameter(description = "可选的访问令牌") String token) throws NoResourceFoundException {
        log.info("user：{}", principal != null ? principal.getName() : null);
        String fileKey = path.substring(1);
        boolean canAccess = fileStoreService.checkAccessible(fileKey,
                principal != null ? Long.valueOf(principal.getName()) : null, token);
        if (!canAccess) {
            throw new ServiceException("Access Denied");
        }
        Optional<FileAccessDTO> fileAccessDTOOptional = fileStoreService.getInputStreamByKey(fileKey);
        if (fileAccessDTOOptional.isEmpty()) {
            throw new NoResourceFoundException(HttpMethod.GET, fileKey, path);
        }
        FileAccessDTO fileAccessDTO = fileAccessDTOOptional.get();

        MediaType mediaType = null;
        if (fileAccessDTO.getContentType() != null) {
            try {
                mediaType = MediaType.parseMediaType(fileAccessDTO.getContentType());
            } catch (Exception ignore) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        String fileName = fileAccessDTO.getName();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20"); // 空格处理

        String contentDisposition = "inline; filename*=UTF-8''" + encodedFileName;
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentLength(fileAccessDTO.getSize())
                .body(new InputStreamResource(fileAccessDTO.getInputStream()));

    }
}
