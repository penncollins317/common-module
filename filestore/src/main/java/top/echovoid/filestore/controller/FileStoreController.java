package top.echovoid.filestore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.echovoid.common.dto.PageDTO;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.params.PageParam;
import top.echovoid.common.utils.FileUtils;
import top.echovoid.filestore.dto.FileUploadRequest;
import top.echovoid.filestore.dto.FileUploadResponse;
import top.echovoid.filestore.service.FileStoreService;
import top.echovoid.oss.dto.FileMetaDTO;
import top.echovoid.oss.enums.AclType;

import java.io.IOException;
import java.security.Principal;

/**
 * 文件管理服务
 *
 * @author Penn Collins
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
    public RestData<FileUploadResponse> uploadApi(@RequestParam MultipartFile file, @RequestParam AclType acl, Principal principal) throws IOException {
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
    @RequestMapping("/meta_info")
    public RestData<FileMetaDTO> metaApi(@RequestParam Long fileId) {
        return RestData.ok(fileStoreService.getMetadata(fileId));
    }


    /**
     * 文件列表
     *
     */
    @RequestMapping("/file_list")
    public RestData<PageDTO<FileMetaDTO>> fileListApi(Principal principal, PageParam pageParam) {
        return RestData.ok(fileStoreService.fileList(Long.valueOf(principal.getName()), pageParam));
    }

}
