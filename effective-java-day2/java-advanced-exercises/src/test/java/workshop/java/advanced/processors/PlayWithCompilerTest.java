package workshop.java.advanced.processors;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.Collections;

/**
 *
 */
public class PlayWithCompilerTest {

    @Test
    public void isntThatCoolTest() throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        System.out.println(compiler.getClass().getName());

        //Get a new instance of the standard file manager implementation
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                null, null, null);

        // Get the list of java file objects, in this case we have only
        Iterable compilationUnits = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(
                Paths.get("TestClass.java").toFile()));

        Assertions.assertThat(compilationUnits).isNotEmpty();
        System.out.println(compilationUnits);
    }
}
