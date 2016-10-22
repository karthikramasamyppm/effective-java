package workshop.java.intermediate.datetime;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class WeeksTest {

    @Test
    public void weekFields() throws Exception {
        LocalDate someDate = LocalDate.of(2016, Month.JANUARY, 1);
        assertThat(someDate.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        assertThat(weekFields.getFirstDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(weekFields.getMinimalDaysInFirstWeek()).isEqualTo(4);
    }

    /**
     * Week one(1) is the week starting on the getFirstDayOfWeek where there are at least getMinimalDaysInFirstWeek() days in the year.
     * If the first week starts after the start of the year then the period before is in the last week of the previous year.
     *
     * For example:
     * - if the 1st day of the year is a Monday, week one starts on the 1st
     * - if the 2nd day of the year is a Monday, week one starts on the 2nd and the 1st is in the last week of the previous year
     * - if the 4th day of the year is a Monday, week one starts on the 4th and the 1st to 3rd is in the last week of the previous year
     * - if the 5th day of the year is a Monday, week two starts on the 5th and the 1st to 4th is in week one

     * @throws Exception
     */
    @Test
    public void weekBasedYear() throws Exception {
        LocalDate someDate = LocalDate.of(2016, Month.JANUARY, 1);
        assertThat(someDate.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        assertThat(someDate.get(weekFields.weekBasedYear())).isEqualTo(2015);
        assertThat(someDate.get(weekFields.weekOfWeekBasedYear())).isEqualTo(53);
    }

    @Test
    public void week() throws Exception {
        LocalDate someDate = LocalDate.of(2016, Month.DECEMBER, 24);
        assertThat(someDate.getDayOfWeek()).isEqualTo(DayOfWeek.SATURDAY);

        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        assertThat(someDate.get(weekFields.weekOfMonth())).isEqualTo(4);
        assertThat(someDate.get(weekFields.weekOfYear())).isEqualTo(51);
        assertThat(someDate.get(weekFields.dayOfWeek())).isEqualTo(6);
    }
}
