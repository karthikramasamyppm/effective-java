package delivery.planning.plan;

import delivery.planning.ProductRefNo;
import demand.forecasting.DemandedAmountsChanged;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
public class PlanCompleteness {

    // state
    private final LocalDate date;
    private ProductAmounts planned;
    private ProductAmounts demands;

    public ProductAmounts plannedAmountsFor(Set<ProductRefNo> subsetOfProducts) {
        return planned
                .subset(subsetOfProducts);
    }

    public ProductAmounts diffFor(Set<ProductRefNo> subsetOfProducts) {
        return demands
                .minus(planned)
                .subset(subsetOfProducts);
    }

    public ProductAmounts diffExcept(Set<ProductRefNo> subsetOfProducts) {
        return demands
                .minus(planned)
                .subsetExcept(subsetOfProducts);
    }

    public void apply(PlannedAmountsChange event) {
        planned = planned.sum(event.getAmountsDiff());
    }

    public void apply(DemandedAmountsChanged event) {
        demands = event.getAmounts();
    }
}
