package top.echovoid.product.controller;

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
 * @author Peng
 * @since 2025/5/10
 */
@RestController
@RequestMapping("/product/categories")
@AuthenticatedRequired
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * 新增产品分类
     */
    @PostMapping
    public RestData<ProductCategoryVO> create(@Valid @RequestBody ProductCategoryDTO dto) {
        ProductCategory category = ProductCategoryConverter.toEntity(dto);
        ProductCategory created = productCategoryService.createCategory(category);
        return RestData.ok(ProductCategoryConverter.toVO(created));
    }

    /**
     * 更新产品分类
     */
    @PutMapping("/{id}")
    public RestData<Boolean> update(@PathVariable Long id, @Valid @RequestBody ProductCategoryDTO dto) {
        ProductCategory category = ProductCategoryConverter.toEntity(dto);
        category.setId(id);
        return RestData.ok(productCategoryService.updateCategory(category));
    }

    /**
     * 删除产品分类
     *
     * @param id 分类ID
     */
    @DeleteMapping("/{id}")
    public RestData<Boolean> delete(@PathVariable Long id) {
        return RestData.ok(productCategoryService.deleteCategory(id));
    }

    /**
     * 根据分类ID获取分类信息
     *
     * @param id 分类ID
     */
    @GetMapping("/{id}")
    public RestData<ProductCategoryVO> get(@PathVariable Long id) {
        return RestData.ok(ProductCategoryConverter.toVO(productCategoryService.getById(id)));
    }

    /**
     * 根据上级分类获取下级分类列表
     *
     * @param parentId 上级分类ID，为空时查询一级分类
     */
    @GetMapping("/parent")
    public RestData<List<ProductCategoryVO>> listByPartnerId(@RequestParam(required = false) Long parentId) {
        List<ProductCategory> list = productCategoryService.listByParentId(parentId);
        return RestData.ok(list.stream().map(ProductCategoryConverter::toVO).toList());
    }


    /**
     * 移动分类
     *
     * @param id       当前分类ID
     * @param targetId 目标分类ID
     */
    @PostMapping("/{id}/move/{targetId}")
    public RestData<Boolean> move(@PathVariable("id") Long id, @PathVariable("targetId") Long targetId) {
        return RestData.ok(productCategoryService.moveCategory(id, targetId));
    }
}
