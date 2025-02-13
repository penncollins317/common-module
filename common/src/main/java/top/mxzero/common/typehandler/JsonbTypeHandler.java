package top.mxzero.common.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import top.mxzero.common.utils.JsonUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@MappedJdbcTypes({JdbcType.OTHER})
@MappedTypes({Map.class})
@Component
public class JsonbTypeHandler extends BaseTypeHandler<Map<String, Object>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        String json = JsonUtils.stringify(parameter);
        if (jdbcType == null) {
            ps.setObject(i, json, java.sql.Types.OTHER); // 使用 SQL 类型 OTHER 来存储 JSONB
        } else {
            ps.setObject(i, json, jdbcType.TYPE_CODE);
        }
    }


    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        if (json != null) {
            JsonUtils.parseMap(rs.getString(columnName), String.class, Object.class);
            return JsonUtils.parseMap(json);
        }
        return null;
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        if (json != null) {
            return JsonUtils.parseMap(json);
        }
        return null; // 如果没有数据，返回 null
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        if (json != null) {
            return JsonUtils.parseMap(json);
        }
        return null; // 如果没有数据，返回 null
    }


}
