package workshop.java.intermediate.fancystuff;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 *
 */
public class SystemTest {

    @Test // Never test performance like that
    // but can be handy in production runtime
    public void nanos() throws Exception {
        // not related to time instant
        long start = System.nanoTime();
        long end = System.nanoTime();

        Assertions.assertThat(Duration.ofNanos(end - start))
                .isBetween(Duration.ZERO, Duration.ofMillis(1));

        // for testing use Java Microbenchmark Harness (JMH) instead
        // <groupId>org.openjdk.jmh</groupId>
        // <artifactId>jmh-core</artifactId>
        // <version>1.15</version>
    }

    @Test // never do that
    public void suggestFinalization() throws Exception {
        System.runFinalization();
    }

    @Test // never do that
    public void suggestGarbageCollection() throws Exception {
        System.gc();
    }
}
