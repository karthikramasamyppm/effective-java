package workshop.java.intermediate.datetime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 */
public class Testability {

    Clock injectedClock = Clock.fixed(Instant.now(), ZoneId.of("Europe/Warsaw"));

    @Test
    public void testability() throws Exception {

        LocalDateTime now1 = LocalDateTime.now(injectedClock);
        Thread.sleep(100);
        LocalDateTime now2 = LocalDateTime.now(injectedClock);

        Assertions.assertThat(now1).isEqualTo(now2);
    }

}
