package top.mxzero.filestore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/upload")
public class FileStoreUploadController {

    private static final long MAX_SIMPLE_UPLOAD_SIZE = 4 * 1024 * 1024; // 4MB
    private static final Path UPLOAD_DIR = Paths.get("/upload-dir");

    // 保存文件上传状态（可换成 Redis）
    private final Map<String, Set<Integer>> uploadedChunks = new ConcurrentHashMap<>();


    public FileStoreUploadController() throws IOException {
        Files.createDirectories(UPLOAD_DIR);
    }

    /**
     * 小文件直传
     */
    @PostMapping("/simple")
    public ResponseEntity<String> uploadSimple(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > MAX_SIMPLE_UPLOAD_SIZE) {
            return ResponseEntity.badRequest().body("File too large for simple upload.");
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path target = UPLOAD_DIR.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok("Uploaded as: " + filename);
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
