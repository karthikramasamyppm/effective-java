package shortages;

import persistence.enums.DeliverySchema;

import java.util.Map;

public class StrategyFactory {
    private static final Map<DeliverySchema, Strategy> mapping = Map.of(
            DeliverySchema.atDayStart, Strategy.AT_DAY_START,
            DeliverySchema.tillEndOfDay, Strategy.TILL_END_OF_DAY
    );

    public Strategy pickCalculationVariant(DeliverySchema schema) {
        return mapping.getOrDefault(
                schema,
                (level, demand1, produced) -> {
                    throw new RuntimeException("Not implemented");
                });
    }
}
