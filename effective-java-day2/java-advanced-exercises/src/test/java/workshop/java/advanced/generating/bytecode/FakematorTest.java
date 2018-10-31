package workshop.java.advanced.generating.bytecode;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 *
 *
 */
public class FakematorTest {

    public static class DooingSomething {

        public String doHardStuff() {
            return "hallo world";
        }
    }

    @Test
    public void testHardStuff() throws Exception {
        DooingSomething fake = new DooingSomething();

        Assertions.assertThat(fake.doHardStuff())
                .isEqualTo("hallo world");
    }

    @Test
    public void testMockedStuff() throws Exception {
        DooingSomething fake = Fakemator.makeFake(DooingSomething.class);

        Assertions.assertThat(fake.doHardStuff())
                .isEqualTo("Fakemator rulezz");
    }
}