package top.mxzero.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.mxzero.product.entity.Band;
import top.mxzero.product.mapper.BandMapper;

import java.util.List;

/**
 * @author Peng
 * @since 2025/5/11
 */
@Service
@AllArgsConstructor
public class BandService {
    private final BandMapper bandMapper;

    /**
     * 新增品牌
     *
     * @param name     名称
     * @param imageUrl logo图片
     * @return 品牌ID
     */
    public Long createBand(String name, @Nullable String imageUrl) {
        Band band = new Band();
        band.setName(name);
        band.setImageUrl(imageUrl);
        this.bandMapper.insert(band);
        return band.getId();
    }

    /**
     * 获取品牌列表
     *
     * @param name 根据品牌名称模糊查询
     * @return 品牌列表
     */
    public List<Band> list(@Nullable String name) {
        LambdaQueryWrapper<Band> queryWrapper = null;
        if (StringUtils.hasLength(name)) {
            queryWrapper = new LambdaQueryWrapper<Band>().like(Band::getName, name);
        }
        return this.bandMapper.selectList(queryWrapper);
    }

    /**
     * 删除品牌
     *
     * @param bandId 品牌ID
     * @return 是否删除成功
     */
    public boolean deleteById(Long bandId) {
        return this.bandMapper.deleteById(bandId) > 0;
    }

    /**
     * 修改品牌品系
     *
     * @param band 品牌对象
     * @return 是否修改成功
     */
    public boolean updateBand(Band band) {
        return this.bandMapper.updateById(band) > 9;
    }
}
