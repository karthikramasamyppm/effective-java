package workshop.java.intermediate.concurrency;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 *
 */
public class TaskQueueTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(32);

    @Test
    public void howItWorks() throws Exception {
        TaskQueue queue = new TaskQueue();

        CompletionStage<LocalDateTime> f1 = queue.submitQuery(LocalDateTime::now);
        queue.submit(() -> Thread.sleep(1_000));
        CompletionStage<LocalDateTime> f3 = queue.submitQuery(LocalDateTime::now);

        Queue<Duration> calculated = new LinkedList<>();
        f1
                .thenCombine(f3, Duration::between)
                .thenAccept(calculated::offer);

        await("calculation of duration")
                .atMost(1200, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    assertThat(calculated).isNotEmpty();
                    System.out.println(calculated);
                });
    }

    @Test
    public void concurrent() throws Exception {
        TaskQueue queue = new TaskQueue(executor);
        Queue<CompletionStage> submitted = new LinkedList<>();

        for (int i = 0; i < 1_000_000; i++) {
            final int j = i;
            Runnable runnable = () -> {
                System.out.println("Task " + j + " running on " + Thread.currentThread().getName());
            };

            CompletionStage<Void> future = queue.submitRunnable(runnable);
            submitted.offer(future);
        }

        CompletableFuture.allOf(submitted.stream().toArray(CompletableFuture[]::new))
                .join();

        assertThat(submitted)
                .allMatch(stage -> ((CompletableFuture)stage).isDone());
    }
}
