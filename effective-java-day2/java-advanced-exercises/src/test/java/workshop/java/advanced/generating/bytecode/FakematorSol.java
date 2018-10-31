package workshop.java.advanced.generating.bytecode;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 *
 */
public class FakematorSol {

    public static <T> T makeFake(Class<T> type) {
        Class<? extends T> fakeClass = new ByteBuddy()
                .subclass(type)
                .method(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                .intercept(MethodDelegation.to(new Interceptor()))
                .make()
                .load(FakematorSol.class.getClassLoader())
                .getLoaded();
        try {
            return fakeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private FakematorSol() {
    }

    private static class Interceptor {
        public String fake() {
            return "Fakemator rulezz";
        }
    }
}
