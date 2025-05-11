package top.mxzero.product.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mxzero.common.annotations.HasRole;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.utils.DeepBeanUtil;
import top.mxzero.product.dto.BandDTO;
import top.mxzero.product.entity.Band;
import top.mxzero.product.service.BandService;

import java.util.List;

/**
 * 品牌接口
 *
 * @author Peng
 * @since 2025/5/11
 */
@RestController
@RequestMapping("/bands")
public class BandController {
    @Autowired
    private BandService bandService;

    /**
     * 新增品牌
     */
    @HasRole("ROLE_ADMIN")
    @PostMapping
    public RestData<String> newBandApi(@Valid @RequestBody BandDTO dto) {
        return RestData.ok(this.bandService.createBand(dto.getName(), dto.getImageUrl()).toString());
    }

    /**
     * 删除品牌
     *
     * @param bandId 品牌ID
     */
    @HasRole("ROLE_ADMIN")
    @DeleteMapping("{bandId:\\d+}")
    public RestData<Boolean> deleteBandApi(@PathVariable("bandId") Long bandId) {
        return RestData.ok(this.bandService.deleteById(bandId));
    }

    /**
     * 修改品牌
     *
     * @param bandId 品牌ID
     */
    @HasRole("ROLE_ADMIN")
    @PutMapping("{bandId:\\d+}")
    public RestData<Boolean> updateBrandApi(@PathVariable("bandId") Long bandId, @Valid @RequestBody BandDTO dto) {
        Band band = DeepBeanUtil.copyProperties(dto, Band::new);
        band.setId(bandId);
        return RestData.ok(this.bandService.updateBand(band));
    }

    /**
     * 品牌列表
     *
     * @param search 查询关键字
     */
    @GetMapping
    public RestData<List<Band>> queryBandApi(@RequestParam(required = false) String search) {
        return RestData.ok(this.bandService.list(search));
    }
}
