package top.mxzero.product.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mxzero.common.dto.RestData;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.product.dto.ProductDTO;
import top.mxzero.product.utils.ProductSpecValidator;

/**
 * 产品接口
 *
 * @author Peng
 * @since 2025/5/13
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    /**
     * 新增产品
     */
    @PostMapping
    public RestData<?> createProductApi(@Valid @RequestBody ProductDTO dto) {
        ProductSpecValidator.validateSkusWithCartesianCheck(dto.getSpecs(), dto.getSkus());
        return RestData.ok(dto);
    }
}
