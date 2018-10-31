package delivery.planning.plan;

import lombok.Value;

import java.time.LocalDate;

@Value
public class PlanningCompleted {
    LocalDate date;
    ProductAmounts reminder;
}
