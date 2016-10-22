package workshop.java.intermediate.datetime;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.Chronology;
import java.time.temporal.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 *
 */
public class DateTimeTest {

    private static final LocalDateTime someDateTime = LocalDateTime.of(
            LocalDate.of(2016, Month.FEBRUARY, 21),
            LocalTime.of(23, 15)
    );

    @Test
    public void currentDateTime() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime utc = LocalDateTime.now(Clock.systemUTC());
        LocalDateTime jap = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

        assertThat(utc).isBeforeOrEqualTo(now);
        assertThat(jap).isAfterOrEqualTo(now);
    }

    @Test
    public void specifiedDate() throws Exception {
        assertThat(
                LocalDateTime.of(2016, Month.FEBRUARY, 21, 23, 15)
        ).isEqualTo(someDateTime);

        assertThat(
                LocalDateTime.of(2016, Month.FEBRUARY, 21, 23, 15, 0)
        ).isEqualTo(someDateTime);

        assertThat(
                LocalDateTime.of(2016, Month.FEBRUARY, 21, 23, 15, 0, 0)
        ).isEqualTo(someDateTime);

        assertThat(
                LocalDateTime.of(2016, 2, 21, 23, 15)
        ).isEqualTo(someDateTime);

        assertThat(
                LocalDateTime.of(
                        LocalDate.of(2016, Month.FEBRUARY, 21),
                        LocalTime.of(23, 15))
        ).isEqualTo(someDateTime);

        assertThat(
                LocalDateTime.ofEpochSecond(1456092900, 0, ZoneOffset.of("+1"))
        ).isEqualTo(someDateTime);

        assertThat(
                LocalDateTime.ofInstant(Instant.ofEpochSecond(1456092900), ZoneId.systemDefault())
        ).isEqualTo(someDateTime);
    }

    @Test
    public void supportedUnits() throws Exception {
        assertThat(someDateTime.isSupported(ChronoUnit.NANOS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.MICROS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.MILLIS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.SECONDS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.MINUTES)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.HOURS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.HALF_DAYS)).isTrue();

        assertThat(someDateTime.isSupported(ChronoUnit.DAYS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.WEEKS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.MONTHS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.YEARS)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.DECADES)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.CENTURIES)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.MILLENNIA)).isTrue();
        assertThat(someDateTime.isSupported(ChronoUnit.ERAS)).isTrue();

        assertThat(someDateTime.isSupported(ChronoUnit.FOREVER)).isFalse();
    }

    @Test
    public void convertOtherConceptsToDateTime() throws Exception {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // by adding missing information
        assertThat(
                LocalDate.of(2016, Month.FEBRUARY, 21)
                        .atTime(23, 15)
        ).isEqualTo(someDateTime);
        assertThat(
                LocalTime.of(23, 15)
                        .atDate(LocalDate.of(2016, Month.FEBRUARY, 21))
        ).isEqualTo(someDateTime);

        // by reducing information
        assertThat(LocalDateTime.from(ZonedDateTime.now(clock))).isEqualTo(LocalDateTime.now(clock));
        assertThat(LocalDateTime.from(OffsetDateTime.now(clock))).isEqualTo(LocalDateTime.now(clock));

        assertThatExceptionOfType(DateTimeException.class).isThrownBy(() ->
                assertThat(LocalDateTime.from(Instant.now(clock))).isEqualTo(LocalDate.now(clock))
        ).withMessageMatching("Unable to obtain LocalDateTime from .+ java.time.Instant");

        assertThat(LocalDateTime.from(Instant.now(clock).atZone(ZoneId.systemDefault())))
                .isEqualTo(LocalDateTime.now(clock));
    }

    @Test
    public void convertDateTimeToOtherConcepts() throws Exception {

        // extend information
        OffsetDateTime offset = someDateTime.atOffset(ZoneOffset.UTC);
        ZonedDateTime zoned = someDateTime.atZone(ZoneId.of("Europe/Warsaw"));

        // reduce information
        int dayOfMonth = someDateTime.getDayOfMonth();
        DayOfWeek dayOfWeek = someDateTime.getDayOfWeek();
        int dayOfYear = someDateTime.getDayOfYear();
        Month month = someDateTime.getMonth();
        int monthValue = someDateTime.getMonthValue();
        int year = someDateTime.getYear();
        int hour = someDateTime.getHour();
        int minute = someDateTime.getMinute();
        int second = someDateTime.getSecond();
        int nano = someDateTime.getNano();

        long epochSecond = someDateTime.toEpochSecond(ZoneOffset.UTC); // to prefix instead of get
        LocalDate localDate = someDateTime.toLocalDate(); // to prefix instead of get
        LocalTime localTime = someDateTime.toLocalTime(); // to prefix instead of get

        // Era era = someDateTime.getEra(); // not available
        Chronology chronology = someDateTime.getChronology(); // Chronology instead of IsoChronology

        // reduce information without convenience methods
        YearMonth yearMonth = YearMonth.from(someDateTime);
        MonthDay monthDay = MonthDay.from(someDateTime);
        DayOfWeek dayOfWeek_ = DayOfWeek.from(someDateTime);
        Month month_ = Month.from(someDateTime);
        Year year_ = Year.from(someDateTime);
    }

    @Test
    public void truncatedTo() throws Exception {
        LocalDateTime precise = someDateTime.withSecond(38).withNano(123456789);
        assertThat(precise).isEqualTo("2016-02-21T23:15:38.123456789");

        assertThat(precise.truncatedTo(ChronoUnit.NANOS)).isEqualTo("2016-02-21T23:15:38.123456789");
        assertThat(precise.truncatedTo(ChronoUnit.MICROS)).isEqualTo("2016-02-21T23:15:38.123456");
        assertThat(precise.truncatedTo(ChronoUnit.MILLIS)).isEqualTo("2016-02-21T23:15:38.123");
        assertThat(precise.truncatedTo(ChronoUnit.SECONDS)).isEqualTo("2016-02-21T23:15:38");
        assertThat(precise.truncatedTo(ChronoUnit.MINUTES)).isEqualTo("2016-02-21T23:15");
        assertThat(precise.truncatedTo(ChronoUnit.HOURS)).isEqualTo("2016-02-21T23:00");
        assertThat(precise.truncatedTo(ChronoUnit.HALF_DAYS)).isEqualTo("2016-02-21T12:00");
    }

    @Test
    public void funkyOne() throws Exception {
        assertThat(someDateTime.getLong(ChronoField.AMPM_OF_DAY)).isEqualTo(1);
        assertThat(someDateTime.getLong(ChronoField.CLOCK_HOUR_OF_AMPM)).isEqualTo(11);
        assertThat(someDateTime.getLong(ChronoField.HOUR_OF_AMPM)).isEqualTo(11);
        assertThat(someDateTime.getLong(ChronoField.NANO_OF_SECOND)).isEqualTo(0);
        assertThat(someDateTime.getLong(ChronoField.NANO_OF_DAY)).isEqualTo(83700000000000L);
        assertThat(someDateTime.getLong(ChronoField.MICRO_OF_SECOND)).isEqualTo(0);
        assertThat(someDateTime.getLong(ChronoField.MICRO_OF_DAY)).isEqualTo(83700000000L);
        assertThat(someDateTime.getLong(ChronoField.MILLI_OF_SECOND)).isEqualTo(0);
        assertThat(someDateTime.getLong(ChronoField.MILLI_OF_DAY)).isEqualTo(83700000);
        assertThat(someDateTime.getLong(ChronoField.SECOND_OF_MINUTE)).isEqualTo(0);
        assertThat(someDateTime.getLong(ChronoField.SECOND_OF_DAY)).isEqualTo(83700);
        assertThat(someDateTime.getLong(ChronoField.SECOND_OF_MINUTE)).isEqualTo(0);
        assertThat(someDateTime.getLong(ChronoField.SECOND_OF_DAY)).isEqualTo(83700);
    }

    @Test
    public void overrideSome() throws Exception {
        assertThat(someDateTime).isEqualTo("2016-02-21T23:15");
        assertThat(someDateTime.withDayOfMonth(2)).isEqualTo("2016-02-02T23:15");
        assertThat(someDateTime.withDayOfYear(2)).isEqualTo("2016-01-02T23:15");
        assertThat(someDateTime.withMonth(12)).isEqualTo("2016-12-21T23:15");
        assertThat(someDateTime.withYear(2022)).isEqualTo("2022-02-21T23:15");
        assertThat(someDateTime.with(ChronoField.DAY_OF_WEEK, 3)).isEqualTo("2016-02-17T23:15");
        assertThat(someDateTime.with(TemporalAdjusters.lastDayOfMonth())).isEqualTo("2016-02-29T23:15");


        assertThat(someDateTime.withHour(2)).isEqualTo("2016-02-21T02:15");
        assertThat(someDateTime.withMinute(2)).isEqualTo("2016-02-21T23:02");
        assertThat(someDateTime.withSecond(12)).isEqualTo("2016-02-21T23:15:12");
        assertThat(someDateTime.withNano(2210212)).isEqualTo("2016-02-21T23:15:00.002210212");
        assertThat(someDateTime.with(ChronoField.SECOND_OF_DAY, 60)).isEqualTo("2016-02-21T00:01");
    }

    @Test
    public void plusAndMinus() throws Exception {
        assertThat(someDateTime).isEqualTo("2016-02-21T23:15");
        assertThat(someDateTime.plusDays(2)).isEqualTo("2016-02-23T23:15");
        assertThat(someDateTime.plusMonths(2)).isEqualTo("2016-04-21T23:15");
        assertThat(someDateTime.plusYears(2)).isEqualTo("2018-02-21T23:15");
        assertThat(someDateTime.plus(2, ChronoUnit.CENTURIES)).isEqualTo("2216-02-21T23:15");
        assertThat(someDateTime.plus(Period.of(1, 2, 25))).isEqualTo("2017-05-16T23:15");
        assertThat(someDateTime.plus(Duration.ofHours(24))).isEqualTo("2016-02-22T23:15");

        assertThat(someDateTime.plusHours(2)).isEqualTo("2016-02-22T01:15");
        assertThat(someDateTime.plusMinutes(2)).isEqualTo("2016-02-21T23:17");
        assertThat(someDateTime.plusSeconds(12)).isEqualTo("2016-02-21T23:15:12");
        assertThat(someDateTime.plusNanos(2210212)).isEqualTo("2016-02-21T23:15:00.002210212");
        assertThat(someDateTime.plus(1, ChronoUnit.CENTURIES)).isEqualTo("2116-02-21T23:15");

        assertThat(someDateTime.minusDays(2)).isEqualTo("2016-02-19T23:15");
        assertThat(someDateTime.minusMonths(2)).isEqualTo("2015-12-21T23:15");
        assertThat(someDateTime.minusYears(2)).isEqualTo("2014-02-21T23:15");
        assertThat(someDateTime.minus(2, ChronoUnit.CENTURIES)).isEqualTo("1816-02-21T23:15");
        assertThat(someDateTime.minus(Period.of(1, 2, 25))).isEqualTo("2014-11-26T23:15");

        assertThat(someDateTime.minus(Duration.ofHours(32))).isEqualTo("2016-02-20T15:15");


        assertThat(someDateTime.minusHours(2)).isEqualTo("2016-02-21T21:15");
        assertThat(someDateTime.minusMinutes(2)).isEqualTo("2016-02-21T23:13");
        assertThat(someDateTime.minusSeconds(12)).isEqualTo("2016-02-21T23:14:48");
        assertThat(someDateTime.minusNanos(2210212)).isEqualTo("2016-02-21T23:14:59.997789788");
        assertThat(someDateTime.minus(1, ChronoUnit.CENTURIES)).isEqualTo("1916-02-21T23:15");
    }

    @Test
    public void adjustInto() throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));

        ZonedDateTime currentTimeAtSomeDate = now.with(someDateTime);
        Temporal temporal = someDateTime.adjustInto(now);

        assertThat(temporal)
                .isInstanceOf(ZonedDateTime.class)
                .isEqualTo(currentTimeAtSomeDate);
    }

    @Test
    public void comparable() throws Exception {
        assertThat(someDateTime.isAfter(LocalDateTime.now())).isFalse();
        assertThat(someDateTime.isBefore(LocalDateTime.now())).isTrue();
        assertThat(someDateTime.compareTo(LocalDateTime.now())).isLessThan(0);
    }

    @Test
    public void durationBetweenDates() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        long until = someDateTime.until(now, ChronoUnit.SECONDS);
        Duration between = Duration.between(someDateTime, now);

        assertThat(until).isEqualTo(between.getSeconds());
    }

    @Test
    public void supportedInformation() throws Exception {
        assertThat(someDateTime.isSupported(ChronoField.NANO_OF_SECOND)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.NANO_OF_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.MICRO_OF_SECOND)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.MICRO_OF_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.MILLI_OF_SECOND)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.MILLI_OF_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.SECOND_OF_MINUTE)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.SECOND_OF_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.MINUTE_OF_HOUR)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.MINUTE_OF_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.HOUR_OF_AMPM)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.CLOCK_HOUR_OF_AMPM)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.HOUR_OF_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.CLOCK_HOUR_OF_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.AMPM_OF_DAY)).isTrue();

        assertThat(someDateTime.isSupported(ChronoField.DAY_OF_WEEK)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.DAY_OF_MONTH)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.DAY_OF_YEAR)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.EPOCH_DAY)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.ALIGNED_WEEK_OF_MONTH)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.ALIGNED_WEEK_OF_YEAR)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.MONTH_OF_YEAR)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.PROLEPTIC_MONTH)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.YEAR_OF_ERA)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.YEAR)).isTrue();
        assertThat(someDateTime.isSupported(ChronoField.ERA)).isTrue();

        assertThat(someDateTime.isSupported(ChronoField.INSTANT_SECONDS)).isFalse();
        assertThat(someDateTime.isSupported(ChronoField.OFFSET_SECONDS)).isFalse();
    }

    @Test
    public void validValueRanges() throws Exception {
        ValueRange range = someDateTime.range(ChronoField.CLOCK_HOUR_OF_AMPM);

        assertThat(range.getMinimum()).isEqualTo(1);
        assertThat(range.getLargestMinimum()).isEqualTo(1);

        assertThat(range.getSmallestMaximum()).isEqualTo(12);
        assertThat(range.getMaximum()).isEqualTo(12);
    }
}
