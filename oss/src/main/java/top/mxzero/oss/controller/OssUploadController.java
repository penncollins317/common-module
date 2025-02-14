package top.mxzero.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.params.PageParam;
import top.mxzero.common.utils.DeepBeanUtil;
import top.mxzero.common.utils.MD5Util;
import top.mxzero.oss.OssProps;
import top.mxzero.oss.dto.OssUploadResult;
import top.mxzero.oss.dto.FileRecordDTO;
import top.mxzero.oss.entity.FileRecord;
import top.mxzero.oss.service.OssService;
import top.mxzero.oss.service.impl.FileRecordService;


import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Peng
 * @since 2024/9/25
 */
@RestController
public class OssUploadController {
    @Autowired
    private OssProps props;
    @Autowired
    private OssService ossService;
    @Autowired
    private FileRecordService fileRecordService;

    @GetMapping("/files")
    public RestData<List<FileRecordDTO>> fileRecordListApi(PageParam param) {
        return RestData.success(DeepBeanUtil.copyProperties(fileRecordService.list(new Page<>(param.getPage(), param.getSize()), new QueryWrapper<FileRecord>().orderByDesc("id")), FileRecordDTO::new));
    }

    @GetMapping("/upload/exists")
    public RestData<Boolean> checkHashApi(@RequestParam("hash") String hash, @RequestParam("content_type") String contentType) {
        FileRecord record = this.fileRecordService.getOne(new QueryWrapper<FileRecord>().eq("hash", hash));
        return RestData.success(record != null && contentType.equals(record.getContentType()));
    }

    @PostMapping("/upload")
    public RestData<OssUploadResult> uploadApi(
            @RequestParam(value = "file") MultipartFile file) throws IOException {
        String hash = MD5Util.getMD5(file.getBytes());

        // 获取当前日期并格式化为年/月/日
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String datePath = now.format(formatter);

        // 生成UUID作为文件名的一部分
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? Paths.get(originalFilename).getFileName().toString().split("\\.")[1] : "";

        // 构建文件名，包含扩展名
        String filename = String.format("%s/%s.%s", datePath, uuid, extension);
        // 上传文件
        OssUploadResult result = this.ossService.upload(file.getInputStream(), filename, file.getContentType(), file.getSize());
        return RestData.success(result);
    }
}
