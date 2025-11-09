package top.echovoid.filestore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import top.echovoid.common.dto.PageDTO;
import top.echovoid.common.params.PageParam;
import top.echovoid.common.utils.DeepBeanUtil;
import top.echovoid.common.utils.FileUtils;
import top.echovoid.filestore.dto.FileAccessDTO;
import top.echovoid.filestore.dto.FileDownloadResponse;
import top.echovoid.filestore.dto.FileUploadRequest;
import top.echovoid.filestore.dto.FileUploadResponse;
import top.echovoid.filestore.service.FileShareService;
import top.echovoid.filestore.service.FileStoreService;
import top.echovoid.filestore.dto.FileMetaDTO;
import top.echovoid.filestore.entity.FileMeta;
import top.echovoid.filestore.enums.AclType;
import top.echovoid.filestore.enums.FileStatus;
import top.echovoid.filestore.mapper.FileMetaMapper;
import top.echovoid.service.user.service.UserService;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Penn Collins
 * @since 2025/9/26
 */
@Slf4j
public class FileSystemFileStoreService implements FileStoreService, ApplicationContextAware {
    private final FileSystemProps props;
    private FileMetaMapper fileMetaMapper;
    private UserService userService;
    private FileShareService fileShareService;

    public FileSystemFileStoreService(FileSystemProps props) {
        this.props = props;
    }

    @Override
    public FileUploadResponse upload(FileUploadRequest request) throws IOException {
        String extendName = FileUtils.getExtendName(request.getFilename());
        String key = DATE_TIME_FORMATTER.format(LocalDateTime.now())
                + UUID.randomUUID().toString().replaceAll("-", "") + "." + extendName;
        File file = new File(props.getPath() + File.separator + key);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (InputStream inputStream = request.getInputStream(); FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        log.info("File upload:{}", file.getPath());
        FileMeta fileMeta = FileMeta.builder()
                .contentType(request.getContentType())
                .name(request.getFilename())
                .storePath(key)
                .size(request.getSize())
                .status(FileStatus.ACTIVE)
                .acl(request.getAcl())
                .userId(request.getUserId())
                .build();
        fileMetaMapper.insert(fileMeta);
        return new FileUploadResponse(fileMeta.getId(), key);
    }

    @Override
    public boolean checkAccessible(Long fileId, @Nullable Long userId, @Nullable String token) {
        FileMeta fileMeta = fileMetaMapper.selectById(fileId);
        if (fileMeta == null) {
            return false;
        }
        if (fileMeta.getAcl() == AclType.PUBLIC) {
            return true;
        }
        if (fileMeta.getAcl() == AclType.PRIVATE && !fileMeta.getUserId().equals(userId)) {
            return false;
        }
        if (fileMeta.getAcl() == AclType.INTERNAL && !userService.getUserRolesByUserId(userId).contains("ROLE_ADMIN")) {
            return false;
        }
        if (fileMeta.getAcl() == AclType.SHARED) {
            // after
        }
        return true;
    }

    @Override
    public boolean checkAccessible(String fileKey, @Nullable Long userId, @Nullable String token) {
        FileMeta fileMeta = fileMetaMapper.selectOne(new QueryWrapper<FileMeta>().eq("store_path", fileKey));
        if (fileMeta == null) {
            return false;
        }
        if (fileMeta.getAcl() == AclType.PUBLIC) {
            return true;
        }
        if (fileMeta.getAcl() == AclType.PRIVATE) {
            if (fileMeta.getUserId().equals(userId)) {
                return true;
            } else if (token != null) {
                return fileShareService.checkToken(fileMeta.getId(), token);
            }
            return false;
        }
        return fileMeta.getAcl() != AclType.INTERNAL || userService.getUserRolesByUserId(userId).contains("ROLE_ADMIN");
    }


    @Override
    public FileDownloadResponse download(Long fileId) {
        return null;
    }

    @Override
    public boolean delete(Long fileId) {
        return false;
    }

    @Override
    public FileMetaDTO getMetadata(Long fileId) {
        FileMeta fileMeta = fileMetaMapper.selectById(fileId);
        return DeepBeanUtil.copyProperties(fileMeta, FileMetaDTO.class);
    }

    @Override
    public String generateAccessUrl(Long fileId, Long expirySeconds) {
        return "";
    }

    @Override
    public Optional<FileAccessDTO> getInputStreamByKey(String key) {

        LambdaQueryWrapper<FileMeta> queryWrapper = new LambdaQueryWrapper<FileMeta>().eq(FileMeta::getStorePath, key);

        FileMeta fileMeta = fileMetaMapper.selectOne(queryWrapper);
        if (fileMeta == null) return Optional.empty();

        try {
            File file = new File(props.getPath() + File.separator + fileMeta.getStorePath());
            FileAccessDTO accessDTO = FileAccessDTO.builder()
                    .inputStream(new FileInputStream(file))
                    .size(fileMeta.getSize())
                    .contentType(fileMeta.getContentType())
                    .name(fileMeta.getName())
                    .build();
            return Optional.of(accessDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageDTO<FileMetaDTO> fileList(Long userId, PageParam pageParam) {
        QueryWrapper<FileMeta> queryWrapper = new QueryWrapper<FileMeta>().eq("user_id", userId).orderByDesc("id");
        Page<FileMeta> fileMetaPage = fileMetaMapper.selectPage(new Page<>(pageParam.getPage(), pageParam.getSize()), queryWrapper);
        return PageDTO.wrap(fileMetaPage, FileMetaDTO::new);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.fileMetaMapper = applicationContext.getBean(FileMetaMapper.class);
        this.userService = applicationContext.getBean(UserService.class);
        this.fileShareService = applicationContext.getBean(FileShareService.class);
    }

    @Getter
    @AllArgsConstructor
    @ConfigurationProperties("echovoid.filestore.local")
    public static class FileSystemProps {
        private String path = "filestore_local_files";
    }
}
