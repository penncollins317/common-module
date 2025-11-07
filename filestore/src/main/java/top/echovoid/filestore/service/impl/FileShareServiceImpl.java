package top.echovoid.filestore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import top.echovoid.common.dto.PageDTO;
import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.common.params.PageParam;
import top.echovoid.common.utils.DeepBeanUtil;
import top.echovoid.common.utils.IpUtil;
import top.echovoid.filestore.dto.FileShareDTO;
import top.echovoid.filestore.dto.FileShareRequestDTO;
import top.echovoid.filestore.entity.FileShared;
import top.echovoid.filestore.mapper.FileShardMapper;
import top.echovoid.filestore.service.FileShareService;
import top.echovoid.oss.dto.FileMetaDTO;
import top.echovoid.oss.entity.FileMeta;
import top.echovoid.oss.mapper.FileMetaMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Peng
 * @since 2025/10/30
 */
@Service
@AllArgsConstructor
public class FileShareServiceImpl implements FileShareService {
    private final FileMetaMapper metaMapper;
    private final FileShardMapper shardMapper;

    @Override
    @Transactional
    public String shareFile(FileShareRequestDTO fileShareDTO) {
        if (fileShareDTO.getExpireAt() != null) {
            long expireMillis = fileShareDTO.getExpireAt().getTime();
            if (expireMillis < System.currentTimeMillis()) {
                throw new ServiceException("分享的过期时间必须在当前时间之后");
            }
        }

        FileMeta fileMeta = metaMapper.selectById(Long.parseLong(fileShareDTO.getFileId()));
        if (fileMeta == null || !fileShareDTO.getUserId().equals(fileMeta.getUserId())) {
            throw new ServiceException("文件不存在");
        }
        String token = UUID.randomUUID().toString();
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(IpUtil.getAccessHost())
                .pathSegment("filestore", "access")
                .pathSegment(fileMeta.getStorePath().split("/"))
                .queryParam("token", token);
        String accessUrl = builder.toUriString();
        FileShared fileShared = FileShared.builder()
                .token(token)
                .userId(fileMeta.getUserId())
                .fileId(fileMeta.getId())
                .url(accessUrl)
                .expireAt(fileShareDTO.getExpireAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        shardMapper.insert(fileShared);
        return accessUrl;
    }

    @Override
    public boolean checkToken(Long fileId, String token) {
        QueryWrapper<FileShared> queryWrapper = new QueryWrapper<FileShared>().eq("file_id", fileId).eq("token", token);
        FileShared fileShared = shardMapper.selectOne(queryWrapper);
        if (fileShared == null) {
            return false;
        }
        if (fileShared.getExpireAt() == null) {
            return true;
        }
        return fileShared.getExpireAt().isAfter(LocalDateTime.now());
    }

    @Override
    public PageDTO<FileShareDTO> shareFileList(Long userId, PageParam pageParam) {
        QueryWrapper<FileShared> queryWrapper = new QueryWrapper<FileShared>().eq("user_id", userId).orderByDesc("id");

        Page<FileShared> page = shardMapper.selectPage(new Page<>(pageParam.getPage(), pageParam.getSize()), queryWrapper);
        List<FileMeta> fileMetaList = metaMapper.selectList(new QueryWrapper<FileMeta>().in("id", page.getRecords().stream().map(FileShared::getFileId).collect(Collectors.toSet())));

        Map<Long, FileMeta> fileMetaMap = fileMetaList.stream().collect(Collectors.toMap(FileMeta::getId, fileMeta -> fileMeta));

        String accessHost = IpUtil.getAccessHost();
        List<FileShareDTO> shareDTOList = page.getRecords().stream().map(fileShared -> {
            FileMeta fileMeta = fileMetaMap.get(fileShared.getFileId());
            return FileShareDTO.builder()
                    .id(fileShared.getId())
                    .fileMetaDTO(fileMeta != null ? DeepBeanUtil.copyProperties(fileMeta, FileMetaDTO::new) : null)
                    .expireAt(fileShared.getExpireAt())
                    .createdAt(fileShared.getCreatedAt())
                    .updatedAt(fileShared.getUpdatedAt())
                    .token(fileShared.getToken())
                    .accessUrl(fileMeta != null ? accessHost + "/filestore/access/" + fileMeta.getStorePath() + "?token=" + fileShared.getToken() : null)
                    .build();
        }).toList();
        return new PageDTO<>(shareDTOList, page.getTotal(), page.getPages());
    }
}
