package workshop.java.intermediate.datetime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Locale;

/**
 *
 */
public class DateRelatedEnums {

    @Test
    public void playWithDayOfWeek() throws Exception {
        DayOfWeek.SATURDAY.getDisplayName(TextStyle.FULL, Locale.getDefault());

        Arrays.stream(DayOfWeek.values())
                .sorted()
                .map(d -> d.getDisplayName(TextStyle.FULL, Locale.getDefault()))
                .forEach(System.out::println);

        DayOfWeek secondDayOfWeek = DayOfWeek.of(2);
        Assertions.assertThat(secondDayOfWeek).isEqualTo(DayOfWeek.TUESDAY);

        DayOfWeek.from(LocalDate.now());
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

        DayOfWeek.MONDAY.adjustInto(LocalDate.now()); // not clear ? ? ?
        LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)); // obvious

        LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY));
    }

    @Test
    public void playWithMonth() throws Exception {
        Month.AUGUST.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault());

        Arrays.stream(Month.values())
                .sorted()
                .map(d -> d.getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()))
                .forEach(System.out::println);

        Month secondMonthOfYear = Month.of(2);
        Assertions.assertThat(secondMonthOfYear).isEqualTo(Month.FEBRUARY);

        Month.from(LocalDate.now());
        Month month = LocalDate.now().getMonth();

        Temporal temporal = Month.AUGUST.adjustInto(LocalDate.now()); // not clear ? ? ?
        Assertions.assertThat(temporal).isEqualTo(
                LocalDate.now().withMonth(Month.AUGUST.getValue())
        );

    }
}
