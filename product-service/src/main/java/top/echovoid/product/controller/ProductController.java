package top.echovoid.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.dto.RestData;
import top.echovoid.product.dto.ProductDTO;
import top.echovoid.product.utils.ProductSpecValidator;

/**
 * 产品接口
 *
 * @author Penn Collins
 * @since 2025/5/13
 */
@Tag(name = "产品接口", description = "提供产品的新增、更新、查询等功能")
@RestController
@RequestMapping("/products")
public class ProductController {
    /**
     * 新增产品
     */
    @Operation(summary = "新增产品", description = "创建一个新的产品，并验证规格与SKU的笛卡尔积")
    @PostMapping
    public RestData<?> createProductApi(@Valid @RequestBody ProductDTO dto) {
        ProductSpecValidator.validateSkusWithCartesianCheck(dto.getSpecs(), dto.getSkus());
        return RestData.ok(dto);
    }
}
