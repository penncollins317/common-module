package top.echovoid.filestore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.filestore.dto.OssUploadResult;
import top.echovoid.filestore.service.OssService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件上传接口
 *
 * @author Penn Collins
 * @since 2025/5/5
 */
@Tag(name = "文件上传接口", description = "提供小文件直传、大文件分片上传及断点续传支持")
@RestController
@RequestMapping("/upload")
public class FileStoreUploadController {
    private final OssService ossService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd");

    private static final long MAX_SIMPLE_UPLOAD_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Path UPLOAD_DIR = Paths.get("/upload-dir");

    private final Map<String, Set<Integer>> uploadedChunks = new ConcurrentHashMap<>();

    public FileStoreUploadController(OssService ossService) throws IOException {
        this.ossService = ossService;
        Files.createDirectories(UPLOAD_DIR);
    }

    /**
     * 小文件直传
     */
    @Operation(summary = "小文件直传", description = "上传 10MB 以内的小文件，返回 OSS 上传结果")
    @PostMapping("/direct")
    public RestData<OssUploadResult> uploadSimple(
            @Parameter(description = "上传的文件") @RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > MAX_SIMPLE_UPLOAD_SIZE) {
            throw new ServiceException("File too large for simple upload.");
        }
        if (file.getOriginalFilename() == null) {
            throw new ServiceException("File name is null.");
        }
        int i = file.getOriginalFilename().lastIndexOf(".");
        String extendName = null;
        if (i == -1) {
            extendName = "";
        } else {
            extendName = file.getOriginalFilename().substring(i);
        }

        String filename = LocalDateTime.now().format(DATE_TIME_FORMATTER) + "/"
                + UUID.randomUUID().toString().replaceAll("-", "") + extendName;
        OssUploadResult result = ossService.upload(file.getInputStream(), filename, file.getContentType(),
                file.getSize());
        return RestData.ok(result);
    }

    /**
     * 上传分片
     */
    @Operation(summary = "上传分片", description = "大文件分片上传接口，每个分片需对应唯一的 fileId 和 chunkIndex")
    @PostMapping("/chunk")
    public ResponseEntity<String> uploadChunk(
            @Parameter(description = "唯一文件标识，用于标识同一次上传的所有分片") @RequestParam("fileId") String fileId,
            @Parameter(description = "当前分片的索引") @RequestParam("chunkIndex") int chunkIndex,
            @Parameter(description = "全部分片总数") @RequestParam("totalChunks") int totalChunks,
            @Parameter(description = "当前分片文件数据") @RequestParam("file") MultipartFile chunkFile) throws IOException {

        Path chunkDir = UPLOAD_DIR.resolve(fileId);
        Files.createDirectories(chunkDir);

        Path chunkPath = chunkDir.resolve("chunk_" + chunkIndex);
        Files.copy(chunkFile.getInputStream(), chunkPath, StandardCopyOption.REPLACE_EXISTING);

        // 标记分片已上传
        uploadedChunks.computeIfAbsent(fileId, k -> Collections.synchronizedSet(new HashSet<>())).add(chunkIndex);

        return ResponseEntity.ok("Chunk " + chunkIndex + " uploaded.");
    }

    /**
     * 查询已上传的分片（断点续传支持）
     */
    @Operation(summary = "查询已上传分片", description = "根据 fileId 查询当前服务器已接收的分片索引列表，用于断点续传")
    @GetMapping("/uploaded-chunks")
    public ResponseEntity<Set<Integer>> getUploadedChunks(
            @Parameter(description = "唯一文件标识") @RequestParam("fileId") String fileId) {
        return ResponseEntity.ok(uploadedChunks.getOrDefault(fileId, Collections.emptySet()));
    }

    /**
     * 合并所有分片
     */
    @Operation(summary = "合并分片", description = "所有分片上传完成后调用此接口，将服务器暂存的分片合并为完整文件")
    @PostMapping("/merge")
    public ResponseEntity<String> mergeChunks(
            @Parameter(description = "唯一文件标识") @RequestParam("fileId") String fileId,
            @Parameter(description = "原始文件名") @RequestParam("filename") String originalFilename,
            @Parameter(description = "全部分片总数") @RequestParam("totalChunks") int totalChunks) throws IOException {

        Path chunkDir = UPLOAD_DIR.resolve(fileId);
        if (!Files.exists(chunkDir)) {
            return ResponseEntity.badRequest().body("Chunks not found.");
        }

        Path mergedFile = UPLOAD_DIR.resolve(UUID.randomUUID() + "_" + originalFilename);
        try (OutputStream out = Files.newOutputStream(mergedFile, StandardOpenOption.CREATE)) {
            for (int i = 0; i < totalChunks; i++) {
                Path chunk = chunkDir.resolve("chunk_" + i);
                if (!Files.exists(chunk)) {
                    return ResponseEntity.badRequest().body("Missing chunk: " + i);
                }
                Files.copy(chunk, out);
            }
        }

        // 清理
        FileSystemUtils.deleteRecursively(chunkDir);
        uploadedChunks.remove(fileId);

        return ResponseEntity.ok("Merged file: " + mergedFile.getFileName());
    }
}
