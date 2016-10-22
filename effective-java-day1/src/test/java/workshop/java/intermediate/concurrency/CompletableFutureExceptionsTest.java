package workshop.java.intermediate.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 *
 */
public class CompletableFutureExceptionsTest {

    @Test
    public void rejectingNextStagesAfterException() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture<Void> postComplete = future.thenAccept(System.out::println);
        future.completeExceptionally(new NullPointerException("something went wrong"));

        assertThatExceptionOfType(ExecutionException.class)
                .isThrownBy(postComplete::get)
                .withCauseInstanceOf(NullPointerException.class)
                .withMessageEndingWith("something went wrong");
    }

    @Test
    public void definitionOfExceptionalFlow() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture<String> withDefault = future.exceptionally(throwable -> "default value");
        future.complete("calculated value");

        String value = withDefault.get();
        assertThat(value).isEqualTo("calculated value");
    }

    @Test
    public void executionOfExceptionalFlow() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture<String> withDefault = future.exceptionally(throwable -> "default value");
        future.completeExceptionally(new NullPointerException("something went wrong"));

        String value = withDefault.get();
        assertThat(value).isEqualTo("default value");

        assertThatExceptionOfType(ExecutionException.class)
                .isThrownBy(future::get)
                .withCauseInstanceOf(NullPointerException.class)
                .withMessageEndingWith("something went wrong");
    }

    @Test
    public void computeAnyOutcomeExecutingPositive() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture<String> withHandler = future.handle((value, throwable) -> {
            if (throwable == null) {
                return value;
            } else {
                System.out.println("got error: " + throwable);
                return "default value";
            }
        });
        future.complete("calculated value");

        String value = withHandler.get();
        assertThat(value).isEqualTo("calculated value");
    }

    @Test
    public void computeAnyOutcomeExecutingExceptional() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture<String> withHandler = future.handle((value, throwable) -> {
            if (throwable == null) {
                return value;
            } else {
                System.out.println("got error: " + throwable);
                return "default value";
            }
        });
        future.completeExceptionally(new NullPointerException("something went wrong"));

        String value = withHandler.get();
        assertThat(value).isEqualTo("default value");
    }

    @Test
    public void consumeAnyOutcomeExecutingPositive() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture<String> withHandler = future.whenComplete((value, throwable) -> {
            if (throwable == null) {
                System.out.println("computed value: " + value);
            } else {
                System.out.println("got error: " + throwable);
            }
        });
        future.complete("calculated value");

        String value = withHandler.get();
        assertThat(value).isEqualTo("calculated value");
    }

    @Test
    public void consumeAnyOutcomeExecutingExceptional() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        CompletableFuture<String> withHandler = future.whenComplete((value, throwable) -> {
            if (throwable == null) {
                System.out.println("computed value:" + value);
            } else {
                System.out.println("got error: " + throwable);
            }
        });
        future.completeExceptionally(new NullPointerException("something went wrong"));

        assertThatExceptionOfType(ExecutionException.class)
                .isThrownBy(withHandler::get)
                .withCauseInstanceOf(NullPointerException.class)
                .withMessageEndingWith("something went wrong");
    }
}
