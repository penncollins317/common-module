package top.mxzero.common.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Peng
 * @since 2025/5/22
 */
@Component
@MappedTypes(Map.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class MapJsonbTypeHandler extends JsonbTypeHandler<Map> {
    public MapJsonbTypeHandler() {
        super(Map.class);
    }
}
