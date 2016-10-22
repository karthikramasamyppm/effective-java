package workshop.java.intermediate.concurrency;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 *
 */
public class ExecutorServiceTest {

    @Test
    public void basic() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Future<String> future = executor.submit(() -> {
            Thread.sleep(100);
            return "Hi from thread !";
        });

        Assertions.assertThat(future.isDone())
                .isFalse();

        Assertions.assertThat(future.get())
                .isEqualTo("Hi from thread !");
    }

    @Test
    public void scheduler() throws Exception {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        ScheduledFuture<String> future = scheduler.schedule(() -> {
            Thread.sleep(5_000);
            return "Hi from thread !";
        }, 1, TimeUnit.SECONDS);

        scheduler.schedule(() -> future.cancel(true), 3, TimeUnit.SECONDS);

        assertThatExceptionOfType(CancellationException.class)
                .isThrownBy(future::get);
    }
}
