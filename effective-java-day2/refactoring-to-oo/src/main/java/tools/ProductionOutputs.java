package tools;

import persistence.entities.ProductionEntity;

import java.time.LocalDate;
import java.util.*;

public class ProductionOutputs {

    private final Map<LocalDate, List<ProductionEntity>> outputs;

    public ProductionOutputs(List<ProductionEntity> productions) {
        outputs = new HashMap<>();
        for (ProductionEntity production : productions) {
            outputs.computeIfAbsent(
                    production.getStart().toLocalDate(),
                    key -> new ArrayList<>())
                    .add(production);
        }
    }

    public long getOutput(LocalDate date) {
        return outputs.getOrDefault(date, Collections.emptyList())
                .stream()
                .mapToLong(ProductionEntity::getOutput)
                .sum();
    }
}
