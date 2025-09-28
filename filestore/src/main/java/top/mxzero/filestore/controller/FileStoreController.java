package top.mxzero.filestore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.FileUtils;
import top.mxzero.filestore.dto.FileAccessDTO;
import top.mxzero.filestore.dto.FileMetadata;
import top.mxzero.filestore.dto.FileUploadRequest;
import top.mxzero.filestore.dto.FileUploadResponse;
import top.mxzero.filestore.service.FileStoreService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Optional;

/**
 * 文件管理服务
 *
 * @author Peng
 * @since 2025/9/26
 */
@Slf4j
@RequestMapping("/filestore")
@RestController
@AllArgsConstructor
public class FileStoreController {
    private final FileStoreService fileStoreService;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public RestData<FileUploadResponse> uploadApi(@RequestParam MultipartFile file, Principal principal, boolean isPublic) throws IOException {
        FileUtils.checkFile(file);
        FileUploadRequest request = FileUploadRequest.builder()
                .filename(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(file.getContentType())
                .inputStream(file.getInputStream())
                .isPublic(isPublic)
                .userId(principal != null ? Long.valueOf(principal.getName()) : null)
                .build();

        return RestData.ok(fileStoreService.upload(request));
    }

    /**
     * 文件元数据
     */
    @RequestMapping("/meta_info")
    public RestData<FileMetadata> metaApi(@RequestParam Long fileId) {
        return RestData.ok(fileStoreService.getMetadata(fileId));
    }

    /**
     * 文件流访问
     *
     * @param path 文件路径
     */
    @RequestMapping("/access/{*path}")
    public ResponseEntity<InputStreamResource> fileAccessApi(@PathVariable String path, Principal principal) throws NoResourceFoundException {
        String fileKey = path.substring(1);
        Optional<FileAccessDTO> fileAccessDTOOptional = fileStoreService.getInputStreamByKey(fileKey);
        if (fileAccessDTOOptional.isEmpty()) {
            throw new NoResourceFoundException(HttpMethod.GET, fileKey);
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
