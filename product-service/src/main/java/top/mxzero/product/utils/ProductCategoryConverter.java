package top.mxzero.product.utils;

import top.mxzero.product.dto.ProductCategoryDTO;
import top.mxzero.product.dto.ProductCategoryVO;
import top.mxzero.product.entity.ProductCategory;

public class ProductCategoryConverter {

    public static ProductCategory toEntity(ProductCategoryDTO dto) {
        ProductCategory entity = new ProductCategory();
        entity.setName(dto.getName());
        entity.setImageUrl(dto.getImageUrl());
        entity.setParentId(dto.getParentId());
        return entity;
    }

    public static ProductCategoryVO toVO(ProductCategory entity) {
        ProductCategoryVO vo = new ProductCategoryVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setParentId(entity.getParentId());
        vo.setImageUrl(entity.getImageUrl());
        vo.setPath(entity.getPath());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setUpdatedAt(entity.getUpdatedAt());
        return vo;
    }
}
