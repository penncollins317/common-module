package top.echovoid.product.utils;

import top.echovoid.common.exceptions.ServiceException;
import top.echovoid.product.dto.ProductSpecDTO;
import top.echovoid.product.dto.ProductVariantDTO;
import top.echovoid.product.dto.SpecValueDTO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Peng
 * @since 2025/5/13
 */
public class ProductSpecValidator {

    public static void validateSkusWithCartesianCheck(List<ProductSpecDTO> specs, List<ProductVariantDTO> skus) {
        if (specs == null || specs.isEmpty() || skus == null || skus.isEmpty()) {
            throw new ServiceException("规格或SKU为空");
        }

        // Step 1: 生成所有合法的规格组合（笛卡尔积）
        List<Map<String, String>> allValidCombinations = generateCartesianProduct(specs);

        // Step 2: 把 SKU 转成 Map<String, String> -> key:value 形式
        Set<String> skuCombinationSet = skus.stream()
                .map(sku -> sku.getSpec().stream()
                        .sorted(Comparator.comparing(SpecValueDTO::getKey))
                        .map(spec -> spec.getKey() + ":" + spec.getValue())
                        .collect(Collectors.joining("|")))
                .collect(Collectors.toSet());

        // Step 3: 把笛卡尔积结果也转成同样形式
        Set<String> validCombinationSet = allValidCombinations.stream()
                .map(map -> map.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .map(e -> e.getKey() + ":" + e.getValue())
                        .collect(Collectors.joining("|")))
                .collect(Collectors.toSet());

        // Step 4: 校验是否完全匹配
        if (!skuCombinationSet.equals(validCombinationSet)) {
            Set<String> missing = new HashSet<>(validCombinationSet);
            missing.removeAll(skuCombinationSet);

            Set<String> extra = new HashSet<>(skuCombinationSet);
            extra.removeAll(validCombinationSet);

            if (!missing.isEmpty()) {
                throw new ServiceException("缺少 SKU 组合：" + missing);

            }
            if (!extra.isEmpty()) {
                throw new ServiceException("多余的 SKU 组合：" + missing);
            }
        }
    }

    private static List<Map<String, String>> generateCartesianProduct(List<ProductSpecDTO> specs) {
        List<Map<String, String>> result = new ArrayList<>();
        generateRecursive(specs, 0, new HashMap<>(), result);
        return result;
    }

    private static void generateRecursive(List<ProductSpecDTO> specs, int index,
                                          Map<String, String> current, List<Map<String, String>> result) {
        if (index == specs.size()) {
            result.add(new LinkedHashMap<>(current));
            return;
        }

        ProductSpecDTO spec = specs.get(index);
        for (String value : spec.getValues()) {
            current.put(spec.getKey(), value);
            generateRecursive(specs, index + 1, current, result);
            current.remove(spec.getKey());
        }
    }
}
