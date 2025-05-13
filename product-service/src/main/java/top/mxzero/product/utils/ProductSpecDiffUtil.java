package top.mxzero.product.utils;

import top.mxzero.product.dto.ProductDTO;
import top.mxzero.product.dto.ProductVariantDTO;
import top.mxzero.product.dto.SpecValueDTO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品规格变化工具类
 *
 * @author Peng
 * @since 2025/5/13
 */
public class ProductSpecDiffUtil {
    public record ProductSkuDiffResult(List<ProductVariantDTO> addedSkus, List<ProductVariantDTO> removedSkus) {
    }

    public static ProductSkuDiffResult compareSkus(ProductDTO oldProduct, ProductDTO newProduct) {
        Map<String, ProductVariantDTO> oldSkuMap = buildSkuKeyMap(oldProduct.getSkus());
        Map<String, ProductVariantDTO> newSkuMap = buildSkuKeyMap(newProduct.getSkus());

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(oldSkuMap.keySet());
        allKeys.addAll(newSkuMap.keySet());

        List<ProductVariantDTO> added = new ArrayList<>();
        List<ProductVariantDTO> removed = new ArrayList<>();

        for (String key : allKeys) {
            boolean inOld = oldSkuMap.containsKey(key);
            boolean inNew = newSkuMap.containsKey(key);
            if (!inOld && inNew) {
                added.add(newSkuMap.get(key));
            } else if (inOld && !inNew) {
                removed.add(oldSkuMap.get(key));
            }
        }

        return new ProductSkuDiffResult(added, removed);
    }

    private static Map<String, ProductVariantDTO> buildSkuKeyMap(List<ProductVariantDTO> skuList) {
        return skuList.stream()
                .collect(Collectors.toMap(
                        ProductSpecDiffUtil::buildSkuKey,
                        sku -> sku,
                        (a, b) -> a // 规避重复 key
                ));
    }

    private static String buildSkuKey(ProductVariantDTO sku) {
        return sku.getSpec().stream()
                .sorted(Comparator.comparing(SpecValueDTO::getKey))
                .map(s -> s.getKey() + ":" + s.getValue())
                .collect(Collectors.joining("|"));
    }
}