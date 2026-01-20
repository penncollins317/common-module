package top.echovoid.common.typehandler;

import lombok.AllArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.Map;

/**
 * @author Penn Collins
 * @since 2025/5/22
 */
@Component
@MappedTypes(Map.class)
@MappedJdbcTypes(JdbcType.OTHER)
@AllArgsConstructor
public class MapJsonbTypeHandler extends BaseTypeHandler<Map<String, Object>> {
    private final ObjectMapper objectMapper;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setObject(i, json, Types.OTHER);
        } catch (JacksonException e) {
            throw new SQLException("Failed to convert object to JSON", e);
        }
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private Map<String, Object> parseJson(String json) throws SQLException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JacksonException e) {
            throw new SQLException("Failed to convert JSON to object", e);
        }
    }
}
