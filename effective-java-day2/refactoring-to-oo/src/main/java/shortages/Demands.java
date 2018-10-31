package shortages;

import persistence.entities.DemandEntity;
import persistence.enums.DeliverySchema;
import tools.Util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demands {

    private final Map<LocalDate, DemandEntity> demandsPerDay;

    public Demands(List<DemandEntity> demands) {
        demandsPerDay = new HashMap<>();
        for (DemandEntity demand1 : demands) {
            demandsPerDay.put(demand1.getDay(), demand1);
        }
    }

    public DailyDemand get(LocalDate date) {
        DemandEntity entity = demandsPerDay.get(date);
        if (entity != null) {
            return new DailyDemand(
                    Util.getDeliverySchema(entity),
                    Util.getLevel(entity)
            );
        } else {
            return new DailyDemand(DeliverySchema.tillEndOfDay, 0);
        }
    }

    public static class DailyDemand {
        private final DeliverySchema deliverySchema;
        private final long level;

        public DailyDemand(DeliverySchema deliverySchema, long level) {
            this.deliverySchema = deliverySchema;
            this.level = level;
        }

        public DeliverySchema getDeliverySchema() {
            return deliverySchema;
        }

        public long getLevel() {
            return level;
        }
    }
}
