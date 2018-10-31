package delivery.planning.plan;

import delivery.planning.ProductRefNo;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
public class PlanCompleteness {

    private final LocalDate date;

    public ProductAmounts plannedAmountsFor(Set<ProductRefNo> subsetOfProducts) {
        return null;
    }

    public ProductAmounts diffFor(Set<ProductRefNo> subsetOfProducts) {
        return null;
    }

    public ProductAmounts diffExcept(Set<ProductRefNo> subsetOfProducts) {
        return null;
    }
}
