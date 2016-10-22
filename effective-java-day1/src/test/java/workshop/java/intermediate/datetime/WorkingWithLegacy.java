package workshop.java.intermediate.datetime;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 *
 */
public class WorkingWithLegacy {

    private final Instant now = Instant.now();
    private final long epochMilli = now.toEpochMilli();

    @Test
    public void javaUtilDate() throws Exception {

        assertThat(
                new java.util.Date(epochMilli).toInstant()
        ).isEqualTo(Instant.ofEpochMilli(epochMilli));

        assertThat(
                java.util.Date.from(now).getTime()
        ).isEqualTo(now.toEpochMilli());

    }

    @Test
    public void javaSQLDate() throws Exception {

        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(
                () -> new java.sql.Date(epochMilli).toInstant()
        );

        assertThat(
                new java.sql.Date(epochMilli).toLocalDate()
        ).isEqualTo(LocalDate.now());

        assertThat(
                new java.sql.Date(epochMilli).getTime() // comment in constructor is worth reading
        ).isEqualTo(epochMilli);

        assertThat(
                java.sql.Date.from(now).getTime()
        ).isEqualTo(now.toEpochMilli());

        assertThat(
                java.sql.Date.from(now)
        ).isEqualTo(new java.sql.Date(epochMilli));

        assertThat(
                java.sql.Date.valueOf(LocalDate.now()).getTime()
        ).isEqualTo(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli())
        .isEqualTo(OffsetDateTime.now()
                .truncatedTo(ChronoUnit.DAYS).toInstant()
                .toEpochMilli());
    }

    @Test
    public void javaSQLTimestamp() throws Exception {

        assertThat(
                new java.sql.Timestamp(epochMilli).toInstant()
        ).isEqualTo(Instant.ofEpochMilli(epochMilli));

        assertThat(
                new java.sql.Timestamp(epochMilli).toLocalDateTime()
        ).isEqualTo(LocalDateTime
                .ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault()));

    }

    @Test
    public void javaCalendar() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(epochMilli);

        assertThat(
                calendar.toInstant()
        ).isEqualTo(Instant.ofEpochMilli(epochMilli));

    }

}
