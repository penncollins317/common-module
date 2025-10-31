package top.mxzero.filestore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.mxzero.common.dto.PageDTO;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.params.PageParam;
import top.mxzero.common.utils.FileUtils;
import top.mxzero.filestore.dto.FileUploadRequest;
import top.mxzero.filestore.dto.FileUploadResponse;
import top.mxzero.filestore.service.FileStoreService;
import top.mxzero.oss.dto.FileMetaDTO;
import top.mxzero.oss.enums.AclType;

import java.io.IOException;
import java.security.Principal;

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
