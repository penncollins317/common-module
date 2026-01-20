package top.echovoid.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.echovoid.common.annotations.AuthenticatedRequired;
import top.echovoid.common.dto.RestData;
import top.echovoid.product.dto.ProductCategoryDTO;
import top.echovoid.product.dto.ProductCategoryVO;
import top.echovoid.product.entity.ProductCategory;
import top.echovoid.product.service.ProductCategoryService;
import top.echovoid.product.utils.ProductCategoryConverter;

import java.util.List;

/**
 * 产品分类接口
 *
 * @author Penn Collins
 * @since 2025/5/10
 */
@Tag(name = "产品分类接口", description = "提供产品分类的新增、修改、删除、查询及层级移动功能")
@RestController
@RequestMapping("/product/categories")
@AuthenticatedRequired
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 新增产品分类
     */
    @Operation(summary = "新增产品分类", description = "创建一个新的产品分类")
    @PostMapping
    public RestData<ProductCategoryVO> create(@Valid @RequestBody ProductCategoryDTO dto) {
        ProductCategory category = ProductCategoryConverter.toEntity(dto);
        ProductCategory created = productCategoryService.createCategory(category);
        return RestData.ok(ProductCategoryConverter.toVO(created));
    }

    /**
     * 更新产品分类
     */
    @Operation(summary = "更新产品分类", description = "根据分类ID更新产品分类信息")
    @PutMapping("/{id}")
    public RestData<Boolean> update(@Parameter(description = "分类ID") @PathVariable Long id,
            @Valid @RequestBody ProductCategoryDTO dto) {
        ProductCategory category = ProductCategoryConverter.toEntity(dto);
        category.setId(id);
        return RestData.ok(productCategoryService.updateCategory(category));
    }

    /**
     * 删除产品分类
     *
     * @param id 分类ID
     */
    @Operation(summary = "删除产品分类", description = "根据分类ID物理或逻辑删除产品分类")
    @DeleteMapping("/{id}")
    public RestData<Boolean> delete(@Parameter(description = "分类ID") @PathVariable Long id) {
        return RestData.ok(productCategoryService.deleteCategory(id));
    }

    /**
     * 根据分类ID获取分类信息
     *
     * @param id 分类ID
     */
    @Operation(summary = "获取分类信息", description = "根据分类ID查询详细的分类信息")
    @GetMapping("/{id}")
    public RestData<ProductCategoryVO> get(@Parameter(description = "分类ID") @PathVariable Long id) {
        return RestData.ok(ProductCategoryConverter.toVO(productCategoryService.getById(id)));
    }

    /**
     * 根据上级分类获取下级分类列表
     *
     * @param parentId 上级分类ID，为空时查询一级分类
     */
    @Operation(summary = "获取下级分类列表", description = "根据父级分类ID查询其直属的所有子分类列表")
    @GetMapping("/parent")
    public RestData<List<ProductCategoryVO>> listByPartnerId(
            @Parameter(description = "父级分类ID") @RequestParam(required = false) Long parentId) {
        List<ProductCategory> list = productCategoryService.listByParentId(parentId);
        return RestData.ok(list.stream().map(ProductCategoryConverter::toVO).toList());
    }

    /**
     * 移动分类
     *
     * @param id       当前分类ID
     * @param targetId 目标分类ID
     */
    @Operation(summary = "移动分类", description = "将指定的分类移动到另一个父级分类下")
    @PostMapping("/{id}/move/{targetId}")
    public RestData<Boolean> move(
            @Parameter(description = "需要移动的分类ID") @PathVariable("id") Long id,
            @Parameter(description = "目标父级分类ID") @PathVariable("targetId") Long targetId) {
        return RestData.ok(productCategoryService.moveCategory(id, targetId));
    }
}
