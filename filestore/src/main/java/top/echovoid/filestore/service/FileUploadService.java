package top.echovoid.filestore.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.echovoid.common.utils.DeepBeanUtil;
import top.echovoid.common.utils.HashUtils;
import top.echovoid.filestore.dto.FileMetaDTO;
import top.echovoid.filestore.dto.OssUploadResult;
import top.echovoid.filestore.entity.FileMeta;
import top.echovoid.filestore.enums.FileStatus;
import top.echovoid.filestore.mapper.FileMetaMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传业务
 *
 * @author Penn Collins
 * @since 2025/4/14
 */
@Service
@AllArgsConstructor
public class FileUploadService {
    private final FileMetaMapper metaMapper;
    private final OssService ossService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd");

    /**
     * 上传随机的文件访问名称
     */
    private String randomFilename(String originName) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        int dotIndex = originName.lastIndexOf(".");
        if (dotIndex > 0) {
            String extension = originName.substring(dotIndex);
            return uuid + extension;
        }
        return uuid;
    }

    /**
     * 文件上传
     *
     * @param file 文件对象
     */
    @Transactional
    public FileMetaDTO upload(MultipartFile file, boolean force) throws IOException {
        byte[] data = file.getBytes();
        String sha256 = HashUtils.sha256(data);
        if (!force) {
            String fileExistsId = this.existFile(sha256);
            if (fileExistsId != null) {
                FileMeta fileMeta = this.metaMapper.selectById(Long.valueOf(fileExistsId));
                return DeepBeanUtil.copyProperties(fileMeta, FileMetaDTO::new);
            }
        }

        FileMeta meta = new FileMeta();
        meta.setSha256(sha256);
        meta.setMd5(HashUtils.md5(data));
        String randomFilename = randomFilename(Objects.requireNonNull(file.getOriginalFilename()));
        meta.setStorePath(DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        meta.setName(file.getOriginalFilename());
        meta.setContentType(file.getContentType());
        meta.setStatus(FileStatus.ACTIVE);
        meta.setSize(file.getSize());

        meta.setCreatedAt(new Date());
        meta.setUpdatedAt(meta.getCreatedAt());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            meta.setUserId(Long.valueOf(authentication.getName()));
        }
        OssUploadResult ossUploadResult = ossService.upload(data, meta.getStorePath() + "/" + randomFilename, file.getContentType());
        meta.setAccessUrl(ossUploadResult.getUrl());
        this.metaMapper.insert(meta);
        return DeepBeanUtil.copyProperties(meta, FileMetaDTO::new);
    }

    /**
     * 判断文件是否存在，文件存在获取一个id
     *
     * @param sha256 文件哈希值
     * @return 文件ID
     */
    @Nullable
    public String existFile(String sha256) {
        QueryWrapper<FileMeta> queryWrapper = new QueryWrapper<FileMeta>().eq("sha256", sha256);
        if (!this.metaMapper.exists(queryWrapper)) {
            return null;
        }
        queryWrapper.orderByDesc("id").select("id");
        FileMeta fileMeta = this.metaMapper.selectPage(new Page<>(1, 1), queryWrapper).getRecords().get(0);
        return fileMeta.getId().toString();
    }

    /**
     * 获取文件元数据
     *
     * @param fileId 文件ID
     */
    public FileMetaDTO getMeta(Long fileId) {
        FileMeta fileMeta = this.metaMapper.selectById(fileId);
        return DeepBeanUtil.copyProperties(fileMeta, FileMetaDTO::new);
    }
}
