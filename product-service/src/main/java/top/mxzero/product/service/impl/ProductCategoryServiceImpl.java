package top.mxzero.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mxzero.common.exceptions.ServiceException;
import top.mxzero.product.entity.ProductCategory;
import top.mxzero.product.mapper.ProductCategoryMapper;
import top.mxzero.product.service.ProductCategoryService;

import java.util.List;

/**
 * @author Peng
 * @since 2025/5/10
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    @Transactional
    public synchronized ProductCategory createCategory(ProductCategory category) {
        String path = null;
        if (category.getParentId() != null) {
            ProductCategory parentCategory = this.productCategoryMapper.selectById(category.getParentId());
            if (parentCategory == null) {
                throw new ServiceException("上级分类不存在");
            }
            if (parentCategory.getPath().split(PATH_SPLIT_FLAG).length >= 3) {
                throw new ServiceException("最大分类层数不得超过3层");
            }
            path = parentCategory.getPath();
        }

        productCategoryMapper.insert(category);
        category.setPath(path != null ? path + PATH_SPLIT_FLAG + category.getId() : category.getId().toString());
        productCategoryMapper.updateById(category);
        return category;
    }

    @Override
    @Transactional
    public synchronized boolean updateCategory(ProductCategory category) {
        if (category.getParentId() != null) {
            ProductCategory parentCategory = this.productCategoryMapper.selectById(category.getParentId());
            if (parentCategory == null) {
                throw new ServiceException("上级分类不存在");
            }
            if (parentCategory.getPath().split(PATH_SPLIT_FLAG).length >= 3) {
                throw new ServiceException("最大分类层数不得超过3层");
            }
            category.setPath(parentCategory.getPath() + PATH_SPLIT_FLAG + category.getId());
        }
        return productCategoryMapper.updateById(category) > 0;
    }

    @Override
    public boolean deleteCategory(Long id) {
        return productCategoryMapper.deleteById(id) > 0;
    }

    @Override
    public ProductCategory getById(Long id) {
        return productCategoryMapper.selectById(id);
    }

    @Override
    public List<ProductCategory> listByParentId(Long parentId) {
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<ProductCategory>().orderByAsc(ProductCategory::getId);
        if (parentId == null) {
            queryWrapper.isNull(ProductCategory::getParentId);
        } else {
            queryWrapper.eq(ProductCategory::getParentId, parentId);
        }
        return productCategoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<ProductCategory> listSubCategories(String pathPrefix) {
        return productCategoryMapper.selectList(
                new LambdaQueryWrapper<ProductCategory>()
                        .likeRight(ProductCategory::getPath, pathPrefix + PATH_SPLIT_FLAG)
                        .orderByAsc(ProductCategory::getPath)
        );
    }

    @Override
    public boolean isNameExists(Long parentId, String name) {
        return productCategoryMapper.exists(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getParentId, parentId)
                        .eq(ProductCategory::getName, name)
        );
    }

    @Override
    @Transactional
    public synchronized boolean moveCategory(Long id, Long targetId) {
        ProductCategory category = productCategoryMapper.selectById(id);
        if (category == null) return false;

        ProductCategory targetCategory = this.productCategoryMapper.selectById(targetId);
        String[] parentPaths = targetCategory.getPath().split(PATH_SPLIT_FLAG);
        if (parentPaths.length >= 3) {
            throw new ServiceException("最大分类层数不得超过3层");
        }
        String newPath = category.getPath() + PATH_SPLIT_FLAG + id;
        // 更新当前分类 path
        category.setPath(newPath);
        category.setParentId(targetId);
        productCategoryMapper.updateById(category);

        // 更新所有子分类 path
        productCategoryMapper.update(new LambdaUpdateWrapper<ProductCategory>()
                .eq(ProductCategory::getParentId, category.getId())
                .set(ProductCategory::getPath, newPath)
        );
        return true;
    }
}
