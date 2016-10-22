package workshop.java.intermediate.datetime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.ValueRange;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 *
 */
public class TimeTest {

    private static final LocalTime someTime = LocalTime.of(23, 15);

    @Test
    public void currentTime() throws Exception {
        LocalTime now = LocalTime.now();
        LocalTime utc = LocalTime.now(Clock.systemUTC());
        LocalTime jap = LocalTime.now(ZoneId.of("Asia/Tokyo"));

        System.out.println(Duration.between(utc, now).toHours());
        System.out.println(Duration.between(now, jap).toHours());
    }

    @Test
    public void specifiedTime() throws Exception {
        assertThat(
                LocalTime.of(23, 15)
        ).isEqualTo(someTime);

        assertThat(
                LocalTime.of(23, 15, 0)
        ).isEqualTo(someTime);

        assertThat(
                LocalTime.of(23, 15, 0, 0)
        ).isEqualTo(someTime);

        assertThat(
                LocalTime.ofSecondOfDay(83700)
        ).isEqualTo(someTime);

        assertThat(
                LocalTime.ofNanoOfDay(83700000000000L)
        ).isEqualTo(someTime);
    }

    @Test
    public void supportedUnits() throws Exception {
        assertThat(someTime.isSupported(ChronoUnit.NANOS)).isTrue();
        assertThat(someTime.isSupported(ChronoUnit.MICROS)).isTrue();
        assertThat(someTime.isSupported(ChronoUnit.MILLIS)).isTrue();
        assertThat(someTime.isSupported(ChronoUnit.SECONDS)).isTrue();
        assertThat(someTime.isSupported(ChronoUnit.MINUTES)).isTrue();
        assertThat(someTime.isSupported(ChronoUnit.HOURS)).isTrue();
        assertThat(someTime.isSupported(ChronoUnit.HALF_DAYS)).isTrue();

        assertThat(someTime.isSupported(ChronoUnit.DAYS)).isFalse();
        assertThat(someTime.isSupported(ChronoUnit.WEEKS)).isFalse();
        assertThat(someTime.isSupported(ChronoUnit.MONTHS)).isFalse();
        assertThat(someTime.isSupported(ChronoUnit.YEARS)).isFalse();
        assertThat(someTime.isSupported(ChronoUnit.DECADES)).isFalse();
        assertThat(someTime.isSupported(ChronoUnit.CENTURIES)).isFalse();
        assertThat(someTime.isSupported(ChronoUnit.MILLENNIA)).isFalse();
        assertThat(someTime.isSupported(ChronoUnit.ERAS)).isFalse();

        assertThat(someTime.isSupported(ChronoUnit.FOREVER)).isFalse();
    }

    @Test
    public void convertOtherConceptsToTime() throws Exception {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        // by adding missing information
        // ? ? ?

        // by reducing information

        assertThat(LocalTime.from(LocalDateTime.now(clock))).isEqualTo(LocalTime.now(clock));
        assertThat(LocalTime.from(OffsetDateTime.now(clock))).isEqualTo(LocalTime.now(clock));
        assertThat(LocalTime.from(ZonedDateTime.now(clock))).isEqualTo(LocalTime.now(clock));

        assertThatExceptionOfType(DateTimeException.class).isThrownBy(() ->
                assertThat(LocalTime.from(Instant.now(clock))).isEqualTo(LocalDate.now(clock))
        ).withMessageMatching("Unable to obtain LocalTime from .+ java.time.Instant");

        assertThat(LocalTime.from(Instant.now(clock).atZone(ZoneId.systemDefault())))
                .isEqualTo(LocalTime.now(clock));
    }

    @Test
    public void convertTimeToOtherConcepts() throws Exception {

        // extend information
        OffsetTime offset = someTime.atOffset(ZoneOffset.UTC);
        LocalDateTime todayAt = someTime.atDate(LocalDate.now());

        // reduce information
        int hour = someTime.getHour();
        int minute = someTime.getMinute();
        int second = someTime.getSecond();
        int nano = someTime.getNano();

        long nanoOfDay = someTime.toNanoOfDay(); // to prefix instead of get
        long secondOfDay = someTime.toSecondOfDay(); // to prefix instead of get

        // Era era = someTime.getEra(); // not available
        // Chronology chronology = someTime.getChronology(); // not available
    }

    @Test
    public void truncat() throws Exception {
        LocalTime precise = someTime.withSecond(38).withNano(123456789);
        assertThat(precise).isEqualTo("23:15:38.123456789");

        assertThat(precise.truncatedTo(ChronoUnit.NANOS)).isEqualTo("23:15:38.123456789");
        assertThat(precise.truncatedTo(ChronoUnit.MICROS)).isEqualTo("23:15:38.123456");
        assertThat(precise.truncatedTo(ChronoUnit.MILLIS)).isEqualTo("23:15:38.123");
        assertThat(precise.truncatedTo(ChronoUnit.SECONDS)).isEqualTo("23:15:38");
        assertThat(precise.truncatedTo(ChronoUnit.MINUTES)).isEqualTo("23:15");
        assertThat(precise.truncatedTo(ChronoUnit.HOURS)).isEqualTo("23:00");
        assertThat(precise.truncatedTo(ChronoUnit.HALF_DAYS)).isEqualTo("12:00");
    }

    @Test
    public void funkyOne() throws Exception {
        assertThat(someTime.getLong(ChronoField.AMPM_OF_DAY)).isEqualTo(1);
        assertThat(someTime.getLong(ChronoField.CLOCK_HOUR_OF_AMPM)).isEqualTo(11);
        assertThat(someTime.getLong(ChronoField.HOUR_OF_AMPM)).isEqualTo(11);
        assertThat(someTime.getLong(ChronoField.NANO_OF_SECOND)).isEqualTo(0);
        assertThat(someTime.getLong(ChronoField.NANO_OF_DAY)).isEqualTo(83700000000000L);
        assertThat(someTime.getLong(ChronoField.MICRO_OF_SECOND)).isEqualTo(0);
        assertThat(someTime.getLong(ChronoField.MICRO_OF_DAY)).isEqualTo(83700000000L);
        assertThat(someTime.getLong(ChronoField.MILLI_OF_SECOND)).isEqualTo(0);
        assertThat(someTime.getLong(ChronoField.MILLI_OF_DAY)).isEqualTo(83700000);
        assertThat(someTime.getLong(ChronoField.SECOND_OF_MINUTE)).isEqualTo(0);
        assertThat(someTime.getLong(ChronoField.SECOND_OF_DAY)).isEqualTo(83700);
        assertThat(someTime.getLong(ChronoField.SECOND_OF_MINUTE)).isEqualTo(0);
        assertThat(someTime.getLong(ChronoField.SECOND_OF_DAY)).isEqualTo(83700);
    }

    @Test
    public void overrideSome() throws Exception {
        assertThat(someTime.withHour(2)).isEqualTo("02:15");
        assertThat(someTime.withMinute(2)).isEqualTo("23:02");
        assertThat(someTime.withSecond(12)).isEqualTo("23:15:12");
        assertThat(someTime.withNano(2210212)).isEqualTo("23:15:00.002210212");
        assertThat(someTime.with(ChronoField.SECOND_OF_DAY, 60)).isEqualTo("00:01");
    }

    @Test
    public void plusAndMinus() throws Exception {
        assertThat(someTime).isEqualTo("23:15");

        assertThat(someTime.plusHours(2)).isEqualTo("01:15");
        assertThat(someTime.plusMinutes(2)).isEqualTo("23:17");
        assertThat(someTime.plusSeconds(12)).isEqualTo("23:15:12");
        assertThat(someTime.plusNanos(2210212)).isEqualTo("23:15:00.002210212");
        assertThat(someTime.plus(1, ChronoUnit.HALF_DAYS)).isEqualTo("11:15");
        assertThat(someTime.plus(Duration.ofHours(24))).isEqualTo("23:15");


        assertThat(someTime.minusHours(2)).isEqualTo("21:15");
        assertThat(someTime.minusMinutes(2)).isEqualTo("23:13");
        assertThat(someTime.minusSeconds(12)).isEqualTo("23:14:48");
        assertThat(someTime.minusNanos(2210212)).isEqualTo("23:14:59.997789788");
        assertThat(someTime.minus(1, ChronoUnit.HALF_DAYS)).isEqualTo("11:15");
        assertThat(someTime.minus(Duration.ofHours(32))).isEqualTo("15:15");
    }

    @Test
    public void adjustInto() throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));

        ZonedDateTime todayAtSomeTime = now.with(someTime);
        Temporal temporal = someTime.adjustInto(now);

        assertThat(temporal)
                .isInstanceOf(ZonedDateTime.class)
                .isEqualTo(todayAtSomeTime);
    }

    @Test
    public void comparable() throws Exception {
        assertThat(someTime.isAfter(LocalTime.NOON)).isTrue();
        assertThat(someTime.isBefore(LocalTime.MIDNIGHT)).isFalse(); // MIDNIGHT is defined as 00:00
        assertThat(someTime.compareTo(LocalTime.NOON)).isGreaterThan(0);
    }

    @Test
    @Disabled("damn, why it fails?")
    public void durationBetweenTimes() throws Exception {
        LocalTime now = LocalTime.now();
        long until = someTime.until(now, ChronoUnit.SECONDS);
        Duration between = Duration.between(someTime, now);

        assertThat(until).isEqualTo(between.getSeconds());
    }

    @Test
    public void supportedInformation() throws Exception {
        assertThat(someTime.isSupported(ChronoField.NANO_OF_SECOND)).isTrue();
        assertThat(someTime.isSupported(ChronoField.NANO_OF_DAY)).isTrue();
        assertThat(someTime.isSupported(ChronoField.MICRO_OF_SECOND)).isTrue();
        assertThat(someTime.isSupported(ChronoField.MICRO_OF_DAY)).isTrue();
        assertThat(someTime.isSupported(ChronoField.MILLI_OF_SECOND)).isTrue();
        assertThat(someTime.isSupported(ChronoField.MILLI_OF_DAY)).isTrue();
        assertThat(someTime.isSupported(ChronoField.SECOND_OF_MINUTE)).isTrue();
        assertThat(someTime.isSupported(ChronoField.SECOND_OF_DAY)).isTrue();
        assertThat(someTime.isSupported(ChronoField.MINUTE_OF_HOUR)).isTrue();
        assertThat(someTime.isSupported(ChronoField.MINUTE_OF_DAY)).isTrue();
        assertThat(someTime.isSupported(ChronoField.HOUR_OF_AMPM)).isTrue();
        assertThat(someTime.isSupported(ChronoField.CLOCK_HOUR_OF_AMPM)).isTrue();
        assertThat(someTime.isSupported(ChronoField.HOUR_OF_DAY)).isTrue();
        assertThat(someTime.isSupported(ChronoField.CLOCK_HOUR_OF_DAY)).isTrue();
        assertThat(someTime.isSupported(ChronoField.AMPM_OF_DAY)).isTrue();

        assertThat(someTime.isSupported(ChronoField.DAY_OF_WEEK)).isFalse();
        assertThat(someTime.isSupported(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)).isFalse();
        assertThat(someTime.isSupported(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR)).isFalse();
        assertThat(someTime.isSupported(ChronoField.DAY_OF_MONTH)).isFalse();
        assertThat(someTime.isSupported(ChronoField.DAY_OF_YEAR)).isFalse();
        assertThat(someTime.isSupported(ChronoField.EPOCH_DAY)).isFalse();
        assertThat(someTime.isSupported(ChronoField.ALIGNED_WEEK_OF_MONTH)).isFalse();
        assertThat(someTime.isSupported(ChronoField.ALIGNED_WEEK_OF_YEAR)).isFalse();
        assertThat(someTime.isSupported(ChronoField.MONTH_OF_YEAR)).isFalse();
        assertThat(someTime.isSupported(ChronoField.PROLEPTIC_MONTH)).isFalse();
        assertThat(someTime.isSupported(ChronoField.YEAR_OF_ERA)).isFalse();
        assertThat(someTime.isSupported(ChronoField.YEAR)).isFalse();
        assertThat(someTime.isSupported(ChronoField.ERA)).isFalse();

        assertThat(someTime.isSupported(ChronoField.INSTANT_SECONDS)).isFalse();
        assertThat(someTime.isSupported(ChronoField.OFFSET_SECONDS)).isFalse();
    }

    @Test
    public void validValueRanges() throws Exception {
        ValueRange range = someTime.range(ChronoField.MICRO_OF_DAY);

        assertThat(range.getMinimum()).isEqualTo(0);
        assertThat(range.getLargestMinimum()).isEqualTo(0);

        assertThat(range.getSmallestMaximum()).isEqualTo(86399999999L);
        assertThat(range.getMaximum()).isEqualTo(86399999999L);
    }
}
