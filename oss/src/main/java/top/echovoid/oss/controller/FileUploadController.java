package top.echovoid.oss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.utils.FileUtils;
import top.echovoid.oss.dto.FileMetaDTO;
import top.echovoid.oss.service.FileUploadService;

import java.io.IOException;

/**
 * 文件上传服务
 *
 * @author Penn Collins
 * @since 2024/9/25
 */
@RestController
public class FileUploadController {
    @Autowired
    private FileUploadService uploadService;

    /**
     * 单文件上传接口
     *
     * @param file 文件对象
     * @param force 是否强制上传，不判断文件是否存在
     */
    @PostMapping("/upload")
    public RestData<FileMetaDTO> uploadApi(@RequestParam(value = "file") MultipartFile file, boolean force) throws IOException {
        FileUtils.checkFile(file);
        return RestData.success(this.uploadService.upload(file, force));
    }

    /**
     * 判断文件是否存在，存在返回文件ID
     *
     * @param sha256 文件SHA-256值
     */
    @RequestMapping("/files/exists")
    public RestData<String> fileExistsApi(@RequestParam String sha256) {
        return RestData.success(this.uploadService.existFile(sha256));
    }

    /**
     * 获取文件元素据
     *
     * @param fileId 文件ID
     */
    @RequestMapping("/files/{fileId:\\d+}")
    public RestData<FileMetaDTO> getFileMetaApi(@PathVariable("fileId") Long fileId) {
        return RestData.success(this.uploadService.getMeta(fileId));
    }
}
