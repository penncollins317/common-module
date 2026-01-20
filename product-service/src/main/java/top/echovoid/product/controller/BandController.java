package top.echovoid.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.annotations.HasRole;
import top.echovoid.common.dto.RestData;
import top.echovoid.common.utils.DeepBeanUtil;
import top.echovoid.product.dto.BandDTO;
import top.echovoid.product.entity.Band;
import top.echovoid.product.service.BandService;

import java.util.List;

/**
 * 品牌接口
 *
 * @author Penn Collins
 * @since 2025/5/11
 */
@Tag(name = "品牌接口", description = "提供品牌的新增、删除、更新及列表查询功能")
@RestController
@RequestMapping("/bands")
public class BandController {
    @Autowired
    private BandService bandService;

    /**
     * 新增品牌
     */
    @Operation(summary = "新增品牌", description = "创建一个新的品牌信息（需要管理员权限）")
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
    @Operation(summary = "删除品牌", description = "根据品牌ID删除指定的品牌（需要管理员权限）")
    @HasRole("ROLE_ADMIN")
    @DeleteMapping("{bandId:\\d+}")
    public RestData<Boolean> deleteBandApi(@Parameter(description = "品牌唯一标识ID") @PathVariable("bandId") Long bandId) {
        return RestData.ok(this.bandService.deleteById(bandId));
    }

    /**
     * 修改品牌
     *
     * @param bandId 品牌ID
     */
    @Operation(summary = "修改品牌", description = "更新指定品牌的基本信息（需要管理员权限）")
    @HasRole("ROLE_ADMIN")
    @PutMapping("{bandId:\\d+}")
    public RestData<Boolean> updateBrandApi(
            @Parameter(description = "品牌唯一标识ID") @PathVariable("bandId") Long bandId,
            @Valid @RequestBody BandDTO dto) {
        Band band = DeepBeanUtil.copyProperties(dto, Band::new);
        band.setId(bandId);
        return RestData.ok(this.bandService.updateBand(band));
    }

    /**
     * 品牌列表
     *
     * @param search 查询关键字
     */
    @Operation(summary = "查询品牌列表", description = "可根据名称关键字搜索品牌列表")
    @GetMapping
    public RestData<List<Band>> queryBandApi(
            @Parameter(description = "搜索关键字（如品牌名称）") @RequestParam(required = false) String search) {
        return RestData.ok(this.bandService.list(search));
    }
}
