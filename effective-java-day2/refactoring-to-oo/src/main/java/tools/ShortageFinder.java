package tools;

import external.CurrentStock;
import persistence.entities.DemandEntity;
import persistence.entities.ProductionEntity;
import persistence.entities.ShortageEntity;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ShortageFinder {

    static StrategyFactory factory;

    /**
     * Production at day of expected delivery is quite complex:
     * We are able to produce and deliver just in time at same day
     * but depending on delivery time or scheme of multiple deliveries,
     * we need to plan properly to have right amount of parts ready before delivery time.
     * <p/>
     * Typical schemas are:
     * <li>Delivery at prod day start</li>
     * <li>Delivery till prod day end</li>
     * <li>Delivery during specified shift</li>
     * <li>Multiple deliveries at specified times</li>
     * Schema changes the way how we calculate shortages.
     * Pick of schema depends on customer demand on daily basis and for each product differently.
     * Some customers includes that information in callof document,
     * other stick to single schema per product. By manual adjustments of demand,
     * customer always specifies desired delivery schema
     * (increase amount in scheduled transport or organize extra transport at given time)
     */
    public static List<ShortageEntity> findShortages(LocalDate today, int daysAhead, CurrentStock stock,
                                                     List<ProductionEntity> productions, List<DemandEntity> demands,
                                                     String productRefNo) {

        List<LocalDate> dates = Stream.iterate(today, date -> date.plusDays(1))
                .limit(daysAhead)
                .collect(toList());

        // Krok 1. Adapter -> Custom Collection 1
        // motywacja: ukrycie operacji na skomplikowanej strukrze
        ProductionOutputs outputs = new ProductionOutputs(productions);

        // Krok 3. Adapter -> Custom Collection 2
        // motywacja: ukrycie operacji na skomplikowanej strukrze
        Demands demandsPerDay = new Demands(demands);

        // TODO ASK including locked or only proper parts
        // TODO ASK current stock or on day start? what if we are in the middle of production a day?
        long level = stock.getLevel();

        List<ShortageEntity> gap = new LinkedList<>();
        for (LocalDate day : dates) {
            // Krok 4. Null Object Pattern
            // motywacja: uproszczenie logiki przez przeniesienie/pozbycie się if (ob != null)
            Demands.DailyDemand demand = demandsPerDay.get(day);
            // Krok 2.  Null Object Pattern
            // motywacja: uproszczenie logiki przez przeniesienie/pozbycie się if (ob != null)
            long produced = outputs.getOutput(day);

            long levelOnDelivery = factory.pickCalculationVariant(demand.getDeliverySchema())
                    .calculateLevelOnDelivery(level, demand, produced);

            if (levelOnDelivery < 0) {
                ShortageEntity entity = new ShortageEntity();
                entity.setRefNo(productRefNo);
                entity.setFound(LocalDate.now());
                entity.setAtDay(day);
                gap.add(entity);
            }
            long endOfDayLevel = level + produced - demand.getLevel();
            // TODO: ASK accumulated shortages or reset when under zero?
            level = endOfDayLevel >= 0 ? endOfDayLevel : 0;
        }
        return gap;
    }

    private ShortageFinder() {
    }
}
