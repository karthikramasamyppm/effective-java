package workshop.java.intermediate.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class AtomicTest {

    AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    public void atomicInt() throws Exception {
        atomicInteger.getAndIncrement();

    }
}
