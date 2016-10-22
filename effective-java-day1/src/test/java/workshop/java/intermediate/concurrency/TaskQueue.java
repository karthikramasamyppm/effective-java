package workshop.java.intermediate.concurrency;

import java.util.Queue;
import java.util.concurrent.*;

/**
 *
 */
public class TaskQueue {

    @FunctionalInterface
    public interface Command {
        void execute() throws Throwable;
    }

    @FunctionalInterface
    public interface Query<T> {
        T execute() throws Throwable;
    }

    private final Executor executor;

    private final Queue<Task> queue = new ConcurrentLinkedQueue<>();
    private final Semaphore semaphore = new Semaphore(1);

    public TaskQueue() {
        executor = Executors.newSingleThreadExecutor();
    }

    public TaskQueue(Executor executor) {
        this.executor = executor;
    }

    public <T> CompletionStage<T> submit(Query<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        queue.offer(new Task<>(future, task));
        execute();
        return future;
    }

    public CompletionStage<Void> submit(Command task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        queue.offer(new Task<>(future, () -> { task.execute(); return null; }));
        execute();
        return future;
    }

    public CompletionStage<Void> submitCommand(Command task) {
        return submit(task);
    }

    public <T> CompletionStage<T> submitQuery(Query<T> task) {
        return submit(task);
    }

    public CompletionStage<Void> submitRunnable(Runnable task) {
        return submit(task::run);
    }

    public <T> CompletionStage<T> submitCallable(Callable<T> task) {
        return submit(task::call);
    }

    private void execute() {
        if (semaphore.tryAcquire()) {
            try {
                executor.execute(() -> {
                    for (Task task = queue.poll(); task != null; task = queue.poll()) {
                        task.call();
                    }
                    semaphore.release();
                });
            } catch (Throwable t) {
                semaphore.release();
                throw t;
            }
        }
    }

    private static class Task <T> {
        private final CompletableFuture<T> future;
        private final Query<T> task;

        public Task(CompletableFuture<T> future, Query<T> task) {
            this.future = future;
            this.task = task;
        }

        private void call() {
            if (future.isCancelled()) {
                return;
            }
            try {
                T value = task.execute();
                future.complete(value);
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        }
    }
}
