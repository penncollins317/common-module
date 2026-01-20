package top.echovoid.filestore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.echovoid.common.dto.PageDTO;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.params.PageParam;
import top.echovoid.common.utils.FileUtils;
import top.echovoid.filestore.dto.FileUploadRequest;
import top.echovoid.filestore.dto.FileUploadResponse;
import top.echovoid.filestore.service.FileStoreService;
import top.echovoid.filestore.dto.FileMetaDTO;
import top.echovoid.filestore.enums.AclType;

import java.io.IOException;
import java.security.Principal;

/**
 * 文件管理服务
 *
 * @author Penn Collins
 * @since 2025/9/26
 */
@Tag(name = "文件管理服务", description = "提供文件上传、元数据查询、文件列表等功能")
@Slf4j
@RequestMapping("/filestore")
@RestController
@AllArgsConstructor
public class FileStoreController {
    private final FileStoreService fileStoreService;

    /**
     * 文件上传
     */
    @Operation(summary = "文件上传", description = "上传文件到指定访问权限的存储中")
    @PostMapping("/upload")
    public RestData<FileUploadResponse> uploadApi(
            @Parameter(description = "上传的文件") @RequestParam MultipartFile file,
            @Parameter(description = "文件的访问控制类型") @RequestParam AclType acl,
            @Parameter(hidden = true) Principal principal) throws IOException {
        FileUtils.checkFile(file);
        FileUploadRequest request = FileUploadRequest.builder()
                .filename(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(file.getContentType())
                .inputStream(file.getInputStream())
                .acl(acl)
                .userId(Long.valueOf(principal.getName()))
                .build();

        return RestData.ok(fileStoreService.upload(request));
    }

    /**
     * 文件元数据
     */
    @Operation(summary = "文件元数据", description = "根据文件ID获取文件的元数据信息")
    @GetMapping("/meta_info")
    public RestData<FileMetaDTO> metaApi(@Parameter(description = "文件唯一标识ID") @RequestParam Long fileId) {
        return RestData.ok(fileStoreService.getMetadata(fileId));
    }

    /**
     * 文件列表
     *
     */
    @Operation(summary = "文件列表", description = "分页获取当前用户的文件列表")
    @GetMapping("/file_list")
    public RestData<PageDTO<FileMetaDTO>> fileListApi(@Parameter(hidden = true) Principal principal,
            PageParam pageParam) {
        return RestData.ok(fileStoreService.fileList(Long.valueOf(principal.getName()), pageParam));
    }

}
