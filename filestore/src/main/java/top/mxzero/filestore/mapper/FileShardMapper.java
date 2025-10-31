package top.mxzero.filestore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import top.mxzero.common.params.PageParam;
import top.mxzero.filestore.entity.FileShared;
import top.mxzero.oss.entity.FileMeta;

import java.util.List;

/**
 * @author Peng
 * @since 2025/10/30
 */
@Repository
public interface FileShardMapper extends BaseMapper<FileShared> {
}