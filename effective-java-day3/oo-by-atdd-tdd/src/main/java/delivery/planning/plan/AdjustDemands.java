package delivery.planning.plan;

import java.time.LocalDate;

public class AdjustDemands {
    private final LocalDate date;
    private final ProductAmounts amounts;

    public AdjustDemands(LocalDate date, ProductAmounts amounts) {
        this.date = date;
        this.amounts = amounts;
    }
}
