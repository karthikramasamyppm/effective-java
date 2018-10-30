package tools;

import persistence.entities.ProductionEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionOutputs {

    private final Map<LocalDate, ProductionEntity> outputs;

    public ProductionOutputs(List<ProductionEntity> productions) {
        outputs = new HashMap<>();
        for (ProductionEntity production : productions) {
            outputs.put(production.getStart().toLocalDate(), production);
        }
    }

    public long getOutput(LocalDate date) {
        ProductionEntity production = outputs.get(date);
        if (production != null) {
            return production.getOutput();
        } else {
            return 0;
        }
    }
}
