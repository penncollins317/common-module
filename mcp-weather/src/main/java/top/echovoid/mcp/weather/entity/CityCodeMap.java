package top.echovoid.mcp.weather.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author Peng
 * @since 2025/4/21
 */
@Data
public class CityCodeMap {
    private Integer id;
    private String name;
    @TableField("city_code")
    private String code;
}
