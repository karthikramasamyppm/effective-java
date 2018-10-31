package delivery.planning.plan;

import delivery.planning.ProductRefNo;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@ToString
public class ProductAmounts {

    private final Map<ProductRefNo, Long> amounts;

    public static ProductAmounts of(Map<ProductRefNo, Long> amounts) {
        return new ProductAmounts(
                amounts.entrySet()
                        .stream()
                        .filter(ProductAmounts::filterEntry)
                        .collect(Collectors.toUnmodifiableMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        )));
    }

    private ProductAmounts(Map<ProductRefNo, Long> amounts) {
        this.amounts = amounts;
    }

    private static boolean filterEntry(Map.Entry<ProductRefNo, Long> entry) {
        return entry.getKey() != null &&
                entry.getValue() != null && entry.getValue() != 0;
    }

    public boolean anyProduct() {
        return amounts.values()
                .stream()
                .anyMatch(value -> value > 0);
    }

    public Set<ProductRefNo> products() {
        return amounts.keySet();
    }

    public ProductAmounts minus(ProductAmounts other) {
        return calculate(other, (amount1, amount2) -> amount1 - amount2);
    }

    public ProductAmounts sum(ProductAmounts other) {
        return calculate(other, (amount1, amount2) -> amount1 + amount2);
    }

    public ProductAmounts subset(Set<ProductRefNo> subsetOfProducts) {
        return filter(e -> subsetOfProducts.contains(e.getKey()));
    }

    public ProductAmounts subsetExcept(Set<ProductRefNo> subsetOfProducts) {
        return filter(e -> !subsetOfProducts.contains(e.getKey()));
    }

    private ProductAmounts calculate(ProductAmounts other, BinaryOperator<Long> operator) {
        return new ProductAmounts(
                Stream.concat(amounts.entrySet().stream(),
                        other.amounts.entrySet().stream()
                ).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        operator
                )));
    }

    private ProductAmounts filter(Predicate<Map.Entry<ProductRefNo, Long>> predicate) {
        return new ProductAmounts(amounts.entrySet()
                .stream()
                .filter(predicate)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                )));
    }
}
