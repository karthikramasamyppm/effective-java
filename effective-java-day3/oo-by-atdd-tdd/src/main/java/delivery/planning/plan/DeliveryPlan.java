package delivery.planning.plan;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class DeliveryPlan {

    // state
    private final LocalDate date;
    private final PlanCompleteness completeness;

    // collaborators
    private final DeliveryEvents events;
    private final DemandForecasting forecasting;

    // behaviour
    public ClosePlanResult close(ClosePlan close) {
        ProductAmounts toAdjust = completeness
                .plannedAmountsFor(close.productsWithDecisionToAdjust());
        if (toAdjust.anyProduct()) {
            forecasting.adjustDemand(new AdjustDemands(date, toAdjust));
        }

        ProductAmounts toRemind = completeness
                .diffFor(close.productsWithDecisionToMakeReminder());
        ProductAmounts restOfDiff = completeness.diffExcept(toRemind.products());

        if (restOfDiff.anyProduct()) {
            return ClosePlanResult.demandsAreNotFulfilled(restOfDiff);
        }

        events.emit(new PlanningCompleted(date, toRemind));
        return ClosePlanResult.closed(date);
    }
}
