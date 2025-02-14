package top.mxzero.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.oss.OssClientType;
import top.mxzero.oss.OssProps;
import top.mxzero.oss.dto.OssUploadResult;
import top.mxzero.oss.dto.PrepareUploadResult;
import top.mxzero.oss.entity.FileRecord;
import top.mxzero.oss.param.PrepareUploadParam;
import top.mxzero.oss.service.OssService;
import top.mxzero.oss.service.impl.FileRecordService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class FileUploadController {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private OssService ossService;

    @Autowired
    private OssProps props;

    @Autowired
    private FileRecordService fileRecordService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 分片文件预处理
     */
    @ResponseBody
    @PostMapping("/upload/prepare")
    public RestData<PrepareUploadResult> prepareUploadApi(@Valid @RequestBody PrepareUploadParam param) {
        // 检查文件hash是否存在
        FileRecord fileRecord = fileRecordService.getOne(new QueryWrapper<FileRecord>().eq("hash", param.getHash()));
        if (fileRecord != null && fileRecord.getSize().equals(param.getSize()) && fileRecord.getContentType().equals(param.getContentType())) {
            return RestData.success(PrepareUploadResult.builder().path(fileRecord.getPath()).hash(param.getHash()).url(fileRecord.getUrl()).state(PrepareUploadResult.STATE.CACHE).build());
        }

        // 预生成文件路径
        String datePath = LocalDate.now().format(DATE_TIME_FORMATTER);

        // 生成UUID作为文件名的一部分
        String fileId = UUID.randomUUID().toString().replaceAll("-", "");

        String extension = param.getName().substring(param.getName().lastIndexOf(".") + 1);

        // 构建文件名，包含扩展名
        String filePath = String.format("%s/%s.%s", datePath, fileId, extension);

        return RestData.success(PrepareUploadResult.builder().path(filePath).hash(param.getHash()).fileId(fileId).state(PrepareUploadResult.STATE.CREATE).build());
    }

    /**
     * 文件分片数据上传
     *
     * @param file       分片文件
     * @param chunkIndex 分片索引
     * @param fileId     文件ID
     */
    @PostMapping("/upload/chunk")
    public ResponseEntity<?> uploadChunk(@RequestParam("file") MultipartFile file, @RequestParam("chunkIndex") int chunkIndex, @RequestParam("fileId") String fileId) {
        // 检查分片是否已上传
        String redisKey = fileId + ":" + chunkIndex;
        if (redisTemplate.hasKey(redisKey)) {
            return ResponseEntity.ok("Chunk already uploaded");
        }

        // 上传分片到 MinIO
        try {
            String objectName = "temp/" + fileId + "/" + chunkIndex;
            ossService.upload(file.getInputStream(), objectName, file.getContentType(), file.getSize());

            // 标记分片已上传
            redisTemplate.opsForValue().set(redisKey, "1", 1, TimeUnit.HOURS); // 设置过期时间

            return ResponseEntity.ok("Chunk uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/upload/complete")
    public RestData<OssUploadResult> completeUpload(@RequestParam("fileId") String fileId, @RequestParam("totalChunks") int totalChunks, @RequestParam("originalFileName") String originalFileName, @RequestParam("fileSize") long fileSize, @RequestParam("contentType") String contentType, @RequestParam("fileHash") String fileHash, @RequestParam("path") String path) throws Exception {
        // 检查所有分片是否已上传
        for (int i = 0; i < totalChunks; i++) {
            String redisKey = fileId + ":" + i;
            if (!redisTemplate.hasKey(redisKey)) {
                throw new ServiceException("Not all chunks uploaded");
            }
        }

        // 先获取分片对象的 URI 列表
        List<String> chunkObjectNames = new ArrayList<>();
        for (int i = 0; i < totalChunks; i++) {
            String chunkObjectName = "temp/" + fileId + "/" + i;
            chunkObjectNames.add(chunkObjectName);
        }

        ossService.compose(path, chunkObjectNames, contentType);

        FileRecord record = new FileRecord();
        record.setPath(path);
        record.setUrl(ossService.prefixName() + path);
        record.setName(originalFileName);
        record.setSize(fileSize);
        record.setHash(fileHash);
        record.setContentType(contentType);
        record.setOrigin("API");
        fileRecordService.save(record);

        Set<Object> keys = redisTemplate.keys(fileId + ":*");
        if (keys != null) {
            // 删除这些键
            redisTemplate.delete(fileId + ":*");
        }

        ossService.removeBatch(chunkObjectNames);
        return RestData.success(OssUploadResult.builder().type(OssClientType.valueOf(props.getType().toUpperCase())).size(fileSize).url(record.getUrl()).bucketName(props.getBucketName()).hash(fileHash).contentType(contentType).build());
    }
}
