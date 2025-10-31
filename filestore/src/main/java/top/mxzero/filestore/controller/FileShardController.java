package top.mxzero.filestore.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.PageDTO;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.params.PageParam;
import top.mxzero.filestore.dto.FileShareDTO;
import top.mxzero.filestore.dto.FileShareRequestDTO;
import top.mxzero.filestore.service.FileShareService;

import java.security.Principal;

/**
 * 文件共享
 *
 * @author Peng
 * @since 2025/10/30
 */
@RestController
@AllArgsConstructor
public class FileShardController {
    private FileShareService shareService;

    /**
     * 文件共享
     *
     */
    @PostMapping("/filestore/share")
    public RestData<String> shareFile(@Valid @RequestBody FileShareRequestDTO fileShareDTO, Principal principal) {
        fileShareDTO.setUserId(Long.valueOf(principal.getName()));
        return RestData.ok(shareService.shareFile(fileShareDTO));
    }


    /**
     * 文件共享列表
     *
     */
    @GetMapping("/filestore/share")
    public RestData<PageDTO<FileShareDTO>> shareFileListApi(PageParam pageParam, Principal principal) {
        return RestData.ok(shareService.shareFileList(Long.valueOf(principal.getName()), pageParam));
    }
}
