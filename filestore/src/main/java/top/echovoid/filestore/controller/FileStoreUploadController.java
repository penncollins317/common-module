package top.echovoid.filestore.controller;

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
@RestController
@RequestMapping("/upload")
public class FileStoreUploadController {
    private final OssService ossService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd");

    private static final long MAX_SIMPLE_UPLOAD_SIZE = 10 * 1024 * 1024; // 10MB
    private static final Path UPLOAD_DIR = Paths.get("/upload-dir");

    // 保存文件上传状态（可换成 Redis）
    private final Map<String, Set<Integer>> uploadedChunks = new ConcurrentHashMap<>();


    public FileStoreUploadController(OssService ossService) throws IOException {
        this.ossService = ossService;
        Files.createDirectories(UPLOAD_DIR);
    }

    /**
     * 小文件直传
     */
    @PostMapping("/direct")
    public RestData<OssUploadResult> uploadSimple(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > MAX_SIMPLE_UPLOAD_SIZE) {
            throw new ServiceException("File too large for simple upload.");
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
        OssUploadResult result = ossService.upload(file.getInputStream(), filename, file.getContentType(), file.getSize());
        return RestData.ok(result);
    }

    /**
     * 上传分片
     */
    @PostMapping("/chunk")
    public ResponseEntity<String> uploadChunk(
            @RequestParam("fileId") String fileId,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("totalChunks") int totalChunks,
            @RequestParam("file") MultipartFile chunkFile) throws IOException {

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
    @GetMapping("/uploaded-chunks")
    public ResponseEntity<Set<Integer>> getUploadedChunks(@RequestParam("fileId") String fileId) {
        return ResponseEntity.ok(uploadedChunks.getOrDefault(fileId, Collections.emptySet()));
    }

    /**
     * 合并所有分片
     */
    @PostMapping("/merge")
    public ResponseEntity<String> mergeChunks(
            @RequestParam("fileId") String fileId,
            @RequestParam("filename") String originalFilename,
            @RequestParam("totalChunks") int totalChunks) throws IOException {

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
