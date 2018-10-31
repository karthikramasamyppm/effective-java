package tools;

import external.CurrentStock;
import external.StockService;
import lombok.AllArgsConstructor;
import persistence.dao.DemandDao;
import persistence.dao.ProductionDao;
import shortages.*;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class ShortageForecastORMRepository implements ShortageForecastRepository {

    private Clock clock;
    private DemandDao demandDao;
    private StockService stockService;
    private ProductionDao productionDao;
    private StrategyFactory factory;

    @Override
    public ShortageForecast get(String productRefNo, int daysAhead) {
        LocalDate today = LocalDate.now(clock);
        List<LocalDate> dates = Stream.iterate(
                today,
                date -> date.plusDays(1))
                .limit(daysAhead)
                .collect(toList());
        CurrentStock stock = stockService.getCurrentStock(productRefNo);

        ProductionOutputs outputs = new ProductionOutputs(
                productionDao.findFromTime(productRefNo, today.atStartOfDay())
        );

        Demands demandsPerDay = new Demands(
                demandDao.findFrom(today.atStartOfDay(), productRefNo)
        );

        return new ShortageForecast(
                stock,
                productRefNo,
                dates,
                outputs,
                demandsPerDay,
                factory
        );
    }
}
