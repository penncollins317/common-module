package top.mxzero.common.typehandler;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public abstract class JsonbTypeHandler<T> extends BaseTypeHandler<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> type;

    public JsonbTypeHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        try {
            // 序列化对象为 JSON 字符串，并使用 Types.OTHER 提交给 PostgreSQL
            String json = objectMapper.writeValueAsString(parameter);
            ps.setObject(i, json, Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to convert object to JSON", e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private T parseJson(String json) throws SQLException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to convert JSON to object", e);
        }
    }
}