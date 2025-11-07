package top.echovoid.security.apikeys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.echovoid.security.apikeys.entity.ApiKey;

/**
 * @author Penn Collins
 * @since 2025/3/4
 */
@Mapper
public interface ApiKeyMapper extends BaseMapper<ApiKey> {
    @Select("SELECT id, app_id, name, code, created_at, updated_at, status FROM api_key where code = #{code}")
    ApiKey findByCode(String code);
}