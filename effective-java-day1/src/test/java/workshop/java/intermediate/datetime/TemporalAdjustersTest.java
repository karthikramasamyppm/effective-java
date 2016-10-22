package workshop.java.intermediate.datetime;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 *
 */
public class TemporalAdjustersTest {

    @Test
    public void testName() throws Exception {


        TemporalAdjusters.firstDayOfMonth();
        TemporalAdjusters.firstDayOfNextMonth();
        TemporalAdjusters.firstDayOfNextYear();
        TemporalAdjusters.firstDayOfYear();
        TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY);

        TemporalAdjusters.lastDayOfMonth();
        TemporalAdjusters.lastDayOfYear();
        TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY);

        TemporalAdjusters.next(DayOfWeek.MONDAY);
        TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY);
        TemporalAdjusters.previous(DayOfWeek.MONDAY);
        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY);

        TemporalAdjusters.ofDateAdjuster(date -> date.withYear(Year.now().getValue()));
    }

    public static TemporalAdjuster firstDayOfYear() {
        return (temporal) -> temporal.with(ChronoField.DAY_OF_YEAR, 1);
    }

    public static TemporalAdjuster firstDayOfNextYear() {
        return (temporal) -> temporal.with(ChronoField.DAY_OF_YEAR, 1).plus(1, ChronoUnit.YEARS);
    }
}
