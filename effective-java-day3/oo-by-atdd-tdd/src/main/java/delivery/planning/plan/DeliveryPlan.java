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
        // calculate what is to adjust
        ProductAmounts toAdjust = completeness
                .plannedAmountsFor(close.productsWithDecisionToAdjust());
        // adjust demands
        if (toAdjust.anyProduct()) {
            forecasting.adjustDemand(new AdjustDemands(date, toAdjust));
        }

        // calculate what is to remind
        ProductAmounts toRemind = completeness
                .diffFor(close.productsWithDecisionToMakeReminder());

        // calculate rest of diff
        ProductAmounts restOfDiff = completeness.diffExcept(toRemind.products());

        // check rest of diff is empty
        // then ClosePlanResult.demandsAreNotFulfilled(diff);
        if (restOfDiff.anyProduct()) {
            return ClosePlanResult.demandsAreNotFulfilled(restOfDiff);
        }

        // planning completed
        events.emit(new PlanningCompleted());
        return ClosePlanResult.closed();
    }
}
