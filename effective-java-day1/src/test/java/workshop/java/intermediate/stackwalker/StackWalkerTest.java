package workshop.java.intermediate.stackwalker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StackWalkerTest {

    @Test
    void checkStack() {
        StackWalker.getInstance().forEach(System.out::println);
    }

    @Test
    void itIsNotWorking() {
        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> StackWalker.getInstance().getCallerClass()
        );
    }

    @Test
    void orMaybe() {
        System.out.println(StackWalker.getInstance(
                StackWalker.Option.RETAIN_CLASS_REFERENCE
        ).getCallerClass());
    }

    @Test
    void junitStack() {
        StackWalker.getInstance(
                StackWalker.Option.RETAIN_CLASS_REFERENCE
        ).walk(stream -> stream
                .dropWhile(f -> f.getDeclaringClass().getPackageName().equals(StackWalkerTest.class.getPackageName()))
                .takeWhile(f -> f.getDeclaringClass().getPackageName().startsWith("org.junit"))
                .peek(System.out::println)
                .count()
        );
    }
}
