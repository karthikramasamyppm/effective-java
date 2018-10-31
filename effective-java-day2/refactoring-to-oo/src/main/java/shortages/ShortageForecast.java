package shortages;

import external.CurrentStock;
import persistence.entities.ShortageEntity;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class ShortageForecast {

    // id produktu dla ktorego liczymy
    private final String productRefNo;

    // data rangew
    private final List<LocalDate> dates;

    // Shortage Forecast
    private final CurrentStock stock;
    private final ProductionOutputs outputs;
    private final Demands demandsPerDay;

    //
    private final StrategyFactory factory;

    public ShortageForecast(CurrentStock stock, String productRefNo, List<LocalDate> dates, ProductionOutputs outputs, Demands demandsPerDay, StrategyFactory factory) {
        this.stock = stock;
        this.productRefNo = productRefNo;
        this.dates = dates;
        this.outputs = outputs;
        this.demandsPerDay = demandsPerDay;
        this.factory = factory;
    }

    public String getProductRefNo() {
        return productRefNo;
    }

    public List<ShortageEntity> findShortages() {
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
}
