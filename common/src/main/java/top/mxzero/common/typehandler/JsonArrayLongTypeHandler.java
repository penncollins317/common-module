package top.mxzero.common.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import top.mxzero.common.utils.JsonUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Peng
 * @email qianmeng6879@163.com
 * @since 2024/1/27
 */
@Component
@MappedTypes(List.class)
public class JsonArrayLongTypeHandler extends BaseTypeHandler<List<Long>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Long> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtils.stringify(parameter));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtils.parseArray(rs.getString(columnName), Long.class);
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtils.parseArray(rs.getString(columnIndex), Long.class);
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtils.parseArray(cs.getString(columnIndex), Long.class);
    }
}