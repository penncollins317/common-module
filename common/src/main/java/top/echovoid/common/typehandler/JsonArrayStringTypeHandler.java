package top.echovoid.common.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import top.echovoid.common.utils.JsonUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Penn Collins
 * @email penncollins317@gmail.com
 * @since 2024/2/19
 */
@AllArgsConstructor
@Component
public class JsonArrayStringTypeHandler extends BaseTypeHandler<List<String>> {
    private final ObjectMapper objectMapper;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtils.parseArray(rs.getString(columnName), String.class);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtils.parseArray(rs.getString(columnIndex), String.class);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtils.parseArray(cs.getString(columnIndex), String.class);
    }
}
