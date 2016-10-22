package workshop.java.intermediate.processes;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcessesTest {

    @Test
    void currentJavaProcess() {
        System.out.println(ProcessHandle.current().info().command());
        System.out.println(ProcessHandle.current().parent()
                .flatMap(processHandle -> processHandle.info().command()));
    }

    @Test
    void allJavaProcesses() {
        ProcessHandle.allProcesses()
                .filter(proc -> proc.info().command()
                        .filter(command -> command.contains("java"))
                        .isPresent())
                .forEach(proc ->
                        System.out.println(proc.pid() + "\t"
                                + proc.parent().map(ProcessHandle::pid).orElse(0L) + "\t"
                                + proc.info().command().orElse(""))
                );
    }

    @Test
    void catchProcessExit() {
        long pid = ProcessHandle.current().pid();

        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() ->
                                ProcessHandle.of(pid).orElseThrow()
                                        .onExit()
                                        .thenAccept(proc -> System.out.println(
                                                "Exiting of " + pid + " still alive: " + proc.isAlive())
                                        )
                        //.join()

                );
    }
}
