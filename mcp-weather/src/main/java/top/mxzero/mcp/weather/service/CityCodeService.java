package top.mxzero.mcp.weather.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import top.mxzero.mcp.weather.entity.CityCodeMap;
import top.mxzero.mcp.weather.mapper.CityCodeMapMapper;

import java.util.List;

/**
 * 城市编码服务
 *
 * @author Peng
 * @since 2025/4/21
 */
@Slf4j
@Service
@AllArgsConstructor
public class CityCodeService {
    private final CityCodeMapMapper cityCodeMapMapper;

    /**
     * 根据城市名称获取城市编码
     *
     * @param city 城市名称，城市名称模糊匹配
     * @return 城市编码号列表
     */
    @Tool(description = "根据城市名称查询城市地区编码号")
    public List<String> queryCityCode(String city) {
        List<CityCodeMap> list = cityCodeMapMapper.selectList(new QueryWrapper<CityCodeMap>().like("name", city));
        if (list.isEmpty()) {
            return List.of();
        }
        return list.stream().map(CityCodeMap::getCode).toList();
    }
}
