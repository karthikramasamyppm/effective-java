package delivery.planning.plan;

import delivery.planning.ProductRefNo;
import lombok.Value;

import java.util.Set;

@Value
public class ClosePlan {
    Set<ProductRefNo> reminderForRefNos;
    Set<ProductRefNo> adjustDemandForRefNos;

    public Set<ProductRefNo> productsWithDecisionToMakeReminder() {
        return reminderForRefNos;
    }

    public Set<ProductRefNo> productsWithDecisionToAdjust() {
        return adjustDemandForRefNos;
    }
}
