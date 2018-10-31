package workshop.java.advanced.processors;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.nio.file.Paths;

/**
 *
 */
public class $EntryTest {

    @Test
    public void isntThatCool() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        System.out.println(compiler.getClass().getName());

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                System.out::println, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(
                Paths.get("TestClass.java").toFile()
        );

        Assertions.assertThat(compilationUnits).isNotEmpty();
        JavaFileObject next = compilationUnits.iterator().next();
    }
}
