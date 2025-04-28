package top.mxzero.multi.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.mxzero.multi.db.entity.LogTable;

/**
 * @author Peng
 * @since 2025/4/25
 */
@Mapper
public interface LogMapper extends BaseMapper<LogTable> {
}
