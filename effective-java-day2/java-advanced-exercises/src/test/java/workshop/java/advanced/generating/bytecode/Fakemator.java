package workshop.java.advanced.generating.bytecode;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 *
 */
public class Fakemator {

    public static <T> T makeFake(Class<T> type) {
        Class<? extends T> fakeClass = new ByteBuddy()
                .subclass(type)
                .method(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                .intercept(MethodDelegation.to(new Fakemator.Interceptor()))
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

    private static class Interceptor {
        @RuntimeType
        public Object intercept(@Origin Method method,
                                @AllArguments Object[] args,
                                @SuperCall Callable<?> callable) throws Throwable {
            return callable.call();
        }
    }

    private Fakemator() {
    }
}
