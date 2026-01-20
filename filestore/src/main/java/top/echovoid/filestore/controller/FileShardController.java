package top.echovoid.filestore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.PageDTO;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.params.PageParam;
import top.echovoid.filestore.dto.FileShareDTO;
import top.echovoid.filestore.dto.FileShareRequestDTO;
import top.echovoid.filestore.service.FileShareService;

import java.security.Principal;

/**
 * 文件共享
 *
 * @author Penn Collins
 * @since 2025/10/30
 */
@Tag(name = "文件共享接口", description = "提供文件的向外共享发起及共享列表查询功能")
@RestController
@AllArgsConstructor
public class FileShardController {
    private FileShareService shareService;

    /**
     * 文件共享
     *
     */
    @Operation(summary = "创建文件共享", description = "为指定文件创建一个限时或永久的共享链接")
    @PostMapping("/filestore/share")
    public RestData<String> shareFile(@Valid @RequestBody FileShareRequestDTO fileShareDTO,
            @Parameter(hidden = true) Principal principal) {
        fileShareDTO.setUserId(Long.valueOf(principal.getName()));
        return RestData.ok(shareService.shareFile(fileShareDTO));
    }

    /**
     * 文件共享列表
     *
     */
    @Operation(summary = "文件共享列表", description = "分页查询当前用户已发起的共享记录列表")
    @GetMapping("/filestore/share")
    public RestData<PageDTO<FileShareDTO>> shareFileListApi(PageParam pageParam,
            @Parameter(hidden = true) Principal principal) {
        return RestData.ok(shareService.shareFileList(Long.valueOf(principal.getName()), pageParam));
    }
}
