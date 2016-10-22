package workshop.java.intermediate.datetime;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.Era;
import java.time.chrono.IsoChronology;
import java.time.temporal.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * classes
 * https://docs.oracle.com/javase/tutorial/datetime/iso/overview.html
 * <p>
 * method naming:
 * https://docs.oracle.com/javase/tutorial/datetime/overview/naming.html
 * <p>
 * <p>
 *
 */
public class DateTest {

    private static final LocalDate someDate = LocalDate.of(2016, Month.FEBRUARY, 21);

    @Test
    public void currentDate() throws Exception {
        LocalDate now = LocalDate.now();
        LocalDate utc = LocalDate.now(Clock.systemUTC());
        LocalDate jap = LocalDate.now(ZoneId.of("Asia/Tokyo"));

        assertThat(utc).isBeforeOrEqualTo(now);
        assertThat(jap).isAfterOrEqualTo(now);
    }

    @Test
    public void specifiedDate() throws Exception {
        assertThat(
                LocalDate.of(2016, Month.FEBRUARY, 21)
        ).isEqualTo(someDate);

        assertThat(
                LocalDate.of(2016, 2, 21)
        ).isEqualTo(someDate);

        assertThat(
                LocalDate.ofYearDay(2016, 52)
        ).isEqualTo(someDate);

        assertThat(
                LocalDate.ofEpochDay(16852)
        ).isEqualTo(someDate);
    }

    @Test
    public void supportedUnits() throws Exception {
        assertThat(someDate.isSupported(ChronoUnit.NANOS)).isFalse();
        assertThat(someDate.isSupported(ChronoUnit.MICROS)).isFalse();
        assertThat(someDate.isSupported(ChronoUnit.MILLIS)).isFalse();
        assertThat(someDate.isSupported(ChronoUnit.SECONDS)).isFalse();
        assertThat(someDate.isSupported(ChronoUnit.MINUTES)).isFalse();
        assertThat(someDate.isSupported(ChronoUnit.HOURS)).isFalse();
        assertThat(someDate.isSupported(ChronoUnit.HALF_DAYS)).isFalse();

        assertThat(someDate.isSupported(ChronoUnit.DAYS)).isTrue();
        assertThat(someDate.isSupported(ChronoUnit.WEEKS)).isTrue();
        assertThat(someDate.isSupported(ChronoUnit.MONTHS)).isTrue();
        assertThat(someDate.isSupported(ChronoUnit.YEARS)).isTrue();
        assertThat(someDate.isSupported(ChronoUnit.DECADES)).isTrue();
        assertThat(someDate.isSupported(ChronoUnit.CENTURIES)).isTrue();
        assertThat(someDate.isSupported(ChronoUnit.MILLENNIA)).isTrue();
        assertThat(someDate.isSupported(ChronoUnit.ERAS)).isTrue();

        assertThat(someDate.isSupported(ChronoUnit.FOREVER)).isFalse();
    }

    @Test
    public void convertOtherConceptsToDate() throws Exception {

        // by adding missing information
        assertThat(
                Year.of(2016)
                        .atMonth(Month.FEBRUARY)
                        .atDay(21)
        ).isEqualTo(someDate);
        assertThat(
                Year.of(2016)
                        .atMonthDay(MonthDay.of(Month.FEBRUARY, 21))
        ).isEqualTo(someDate);
        assertThat(
                Year.of(2016)
                        .atDay(52)
        ).isEqualTo(someDate);
        assertThat(
                MonthDay.of(Month.FEBRUARY, 21)
                        .atYear(2016)
        ).isEqualTo(someDate);
        assertThat(
                YearMonth.of(2016, Month.FEBRUARY)
                        .atDay(21)
        ).isEqualTo(someDate);

        // by reducing information
        assertThat(LocalDate.from(LocalDateTime.now())).isEqualTo(LocalDate.now());
        assertThat(LocalDate.from(ZonedDateTime.now())).isEqualTo(LocalDate.now());
        assertThat(LocalDate.from(OffsetDateTime.now())).isEqualTo(LocalDate.now());

        assertThatExceptionOfType(DateTimeException.class).isThrownBy(() ->
                assertThat(LocalDate.from(Instant.now())).isEqualTo(LocalDate.now())
        ).withMessageMatching("Unable to obtain LocalDate from .+ java.time.Instant");

        assertThat(LocalDate.from(Instant.now().atZone(ZoneId.systemDefault()))).isEqualTo(LocalDate.now());
    }

    @Test
    public void convertDateToOtherConcepts() throws Exception {

        // extend information
        LocalDateTime startOfDay = someDate.atStartOfDay();
        LocalDateTime endOfDay = someDate.atTime(23, 59, 59);

        // reduce information
        int dayOfMonth = someDate.getDayOfMonth();
        DayOfWeek dayOfWeek = someDate.getDayOfWeek();
        int dayOfYear = someDate.getDayOfYear();
        Month month = someDate.getMonth();
        int monthValue = someDate.getMonthValue();
        int year = someDate.getYear();
        long epochDay = someDate.toEpochDay(); // to prefix instead of get

        Era era = someDate.getEra();
        IsoChronology chronology = someDate.getChronology();

        // reduce information without convenience methods
        YearMonth yearMonth = YearMonth.from(someDate);
        MonthDay monthDay = MonthDay.from(someDate);
        DayOfWeek dayOfWeek_ = DayOfWeek.from(someDate);
        Month month_ = Month.from(someDate);
        Year year_ = Year.from(someDate);
    }

    @Test
    public void funkyOne() throws Exception {
        assertThat(someDate.getLong(ChronoField.PROLEPTIC_MONTH)).isEqualTo(24193);
        assertThat(someDate.getLong(ChronoField.YEAR_OF_ERA)).isEqualTo(2016);
        assertThat(someDate.getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).isEqualTo(7);
        assertThat(someDate.getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).isEqualTo(3);
        assertThat(someDate.getLong(ChronoField.ALIGNED_WEEK_OF_MONTH)).isEqualTo(3);
        assertThat(someDate.getLong(ChronoField.ALIGNED_WEEK_OF_YEAR)).isEqualTo(8);
    }

    @Test
    public void handyMethods() throws Exception {
        int lengthOfMonth = someDate.lengthOfMonth();
        int lengthOfYear = someDate.lengthOfYear();
        boolean leapYear = someDate.isLeapYear();
    }

    @Test
    public void overrideSome() throws Exception {
        assertThat(someDate).isEqualTo("2016-02-21");
        assertThat(someDate.withDayOfMonth(2)).isEqualTo("2016-02-02");
        assertThat(someDate.withDayOfYear(2)).isEqualTo("2016-01-02");
        assertThat(someDate.withMonth(12)).isEqualTo("2016-12-21");
        assertThat(someDate.withYear(2022)).isEqualTo("2022-02-21");
        assertThat(someDate.with(ChronoField.DAY_OF_WEEK, 3)).isEqualTo("2016-02-17");
        assertThat(someDate.with(TemporalAdjusters.lastDayOfMonth())).isEqualTo("2016-02-29");
    }

    @Test
    public void plusAndMinus() throws Exception {
        assertThat(someDate.plusDays(2)).isEqualTo("2016-02-23");
        assertThat(someDate.plusMonths(2)).isEqualTo("2016-04-21");
        assertThat(someDate.plusYears(2)).isEqualTo("2018-02-21");
        assertThat(someDate.plus(2, ChronoUnit.CENTURIES)).isEqualTo("2216-02-21");
        assertThat(someDate.plus(Period.of(1, 2, 25))).isEqualTo("2017-05-16");

        assertThatExceptionOfType(UnsupportedTemporalTypeException.class).isThrownBy(
                () -> someDate.plus(Duration.ofHours(24))
        ).withMessage("Unsupported unit: Seconds");

        assertThat(someDate.minusDays(2)).isEqualTo("2016-02-19");
        assertThat(someDate.minusMonths(2)).isEqualTo("2015-12-21");
        assertThat(someDate.minusYears(2)).isEqualTo("2014-02-21");
        assertThat(someDate.minus(2, ChronoUnit.CENTURIES)).isEqualTo("1816-02-21");
        assertThat(someDate.minus(Period.of(1, 2, 25))).isEqualTo("2014-11-26");

        assertThatExceptionOfType(UnsupportedTemporalTypeException.class).isThrownBy(
                () -> someDate.minus(Duration.ofHours(32))
        ).withMessage("Unsupported unit: Seconds");
    }

    @Test
    public void adjustInto() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime currentTimeAtSomeDate = now.with(someDate);
        Temporal temporal = someDate.adjustInto(now);

        assertThat(temporal)
                .isInstanceOf(LocalDateTime.class)
                .isEqualTo(currentTimeAtSomeDate);
    }

    @Test
    public void comparable() throws Exception {
        assertThat(someDate.isAfter(LocalDate.now())).isFalse();
        assertThat(someDate.isBefore(LocalDate.now())).isTrue();
        assertThat(someDate.compareTo(LocalDate.now())).isLessThan(0);
    }

    @Test
    public void periodBetweenDates() throws Exception {
        LocalDate now = LocalDate.now();
        Period until = someDate.until(now);
        Period between = Period.between(someDate, now);

        assertThat(until).isEqualTo(between);
    }

    @Test
    public void supportedInformation() throws Exception {
        assertThat(someDate.isSupported(ChronoField.NANO_OF_SECOND)).isFalse();
        assertThat(someDate.isSupported(ChronoField.NANO_OF_DAY)).isFalse();
        assertThat(someDate.isSupported(ChronoField.MICRO_OF_SECOND)).isFalse();
        assertThat(someDate.isSupported(ChronoField.MICRO_OF_DAY)).isFalse();
        assertThat(someDate.isSupported(ChronoField.MILLI_OF_SECOND)).isFalse();
        assertThat(someDate.isSupported(ChronoField.MILLI_OF_DAY)).isFalse();
        assertThat(someDate.isSupported(ChronoField.SECOND_OF_MINUTE)).isFalse();
        assertThat(someDate.isSupported(ChronoField.SECOND_OF_DAY)).isFalse();
        assertThat(someDate.isSupported(ChronoField.MINUTE_OF_HOUR)).isFalse();
        assertThat(someDate.isSupported(ChronoField.MINUTE_OF_DAY)).isFalse();
        assertThat(someDate.isSupported(ChronoField.HOUR_OF_AMPM)).isFalse();
        assertThat(someDate.isSupported(ChronoField.CLOCK_HOUR_OF_AMPM)).isFalse();
        assertThat(someDate.isSupported(ChronoField.HOUR_OF_DAY)).isFalse();
        assertThat(someDate.isSupported(ChronoField.CLOCK_HOUR_OF_DAY)).isFalse();
        assertThat(someDate.isSupported(ChronoField.AMPM_OF_DAY)).isFalse();

        assertThat(someDate.isSupported(ChronoField.DAY_OF_WEEK)).isTrue();
        assertThat(someDate.isSupported(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).isTrue();
        assertThat(someDate.isSupported(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).isTrue();
        assertThat(someDate.isSupported(ChronoField.DAY_OF_MONTH)).isTrue();
        assertThat(someDate.isSupported(ChronoField.DAY_OF_YEAR)).isTrue();
        assertThat(someDate.isSupported(ChronoField.EPOCH_DAY)).isTrue();
        assertThat(someDate.isSupported(ChronoField.ALIGNED_WEEK_OF_MONTH)).isTrue();
        assertThat(someDate.isSupported(ChronoField.ALIGNED_WEEK_OF_YEAR)).isTrue();
        assertThat(someDate.isSupported(ChronoField.MONTH_OF_YEAR)).isTrue();
        assertThat(someDate.isSupported(ChronoField.PROLEPTIC_MONTH)).isTrue();
        assertThat(someDate.isSupported(ChronoField.YEAR_OF_ERA)).isTrue();
        assertThat(someDate.isSupported(ChronoField.YEAR)).isTrue();
        assertThat(someDate.isSupported(ChronoField.ERA)).isTrue();

        assertThat(someDate.isSupported(ChronoField.INSTANT_SECONDS)).isFalse();
        assertThat(someDate.isSupported(ChronoField.OFFSET_SECONDS)).isFalse();
    }

    @Test
    public void validValueRanges() throws Exception {
        ValueRange range = someDate.range(ChronoField.DAY_OF_MONTH);

        assertThat(range.getMinimum()).isEqualTo(1);
        assertThat(range.getLargestMinimum()).isEqualTo(1);

        assertThat(range.getSmallestMaximum()).isEqualTo(29);
        assertThat(range.getMaximum()).isEqualTo(29);

        range = someDate.withYear(2017).range(ChronoField.DAY_OF_MONTH);

        assertThat(range.getMinimum()).isEqualTo(1);
        assertThat(range.getLargestMinimum()).isEqualTo(1);

        assertThat(range.getSmallestMaximum()).isEqualTo(28);
        assertThat(range.getMaximum()).isEqualTo(28);

        range = MonthDay.of(Month.FEBRUARY, 2).range(ChronoField.DAY_OF_MONTH);

        assertThat(range.getMinimum()).isEqualTo(1);
        assertThat(range.getLargestMinimum()).isEqualTo(1);

        assertThat(range.getSmallestMaximum()).isEqualTo(28);
        assertThat(range.getMaximum()).isEqualTo(29);
    }
}
