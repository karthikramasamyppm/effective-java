package delivery.planning.plan;

import lombok.Value;

import java.time.LocalDate;

@Value
public class AdjustDemands {
    LocalDate date;
    ProductAmounts amounts;
}
