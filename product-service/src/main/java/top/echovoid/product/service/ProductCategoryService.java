package top.echovoid.product.service;

import top.echovoid.product.entity.ProductCategory;

import java.util.List;

/**
 * @author Peng
 * @since 2025/5/10
 */
public interface ProductCategoryService {
    String PATH_SPLIT_FLAG = "/";

    // 新增分类
    ProductCategory createCategory(ProductCategory category);

    // 修改分类
    boolean updateCategory(ProductCategory category);

    // 删除分类（可选择逻辑删除）
    boolean deleteCategory(Long id);

    // 根据 ID 获取分类
    ProductCategory getById(Long id);

    // 查询商户下所有分类（带排序）
    List<ProductCategory> listByParentId(Long parentId);

    // 查询某分类的所有子分类
    List<ProductCategory> listSubCategories(String pathPrefix);

    // 检查分类名是否重复（在同一个 parent 下）
    boolean isNameExists(Long parentId, String name);

    // 移动分类（修改 path）
    boolean moveCategory(Long id, Long targetId);
}

