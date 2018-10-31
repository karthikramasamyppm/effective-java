package workshop.java.advanced.javaagents;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 *
 */
public class HackyAgent {

    public static void premain(String arguments, Instrumentation instrumentation) {
        System.out.print("starting agent: premain");
        new AgentBuilder.Default()
                .type(ElementMatchers.nameEndsWith("Hackable"))
                .transform((builder, type, classLoader) ->
                        builder.method(ElementMatchers.any())
                                .intercept(MethodDelegation.to(Interceptor.class))
                ).installOn(instrumentation);
    }

    public static void agentmain(String arguments, Instrumentation instrumentation) {
        System.out.print("starting agent: premain");
        premain(arguments, instrumentation);
    }

    private static class Interceptor {

        @RuntimeType
        public static Object intercept(@Origin Method method,
                                       @AllArguments Object[] args,
                                       @SuperCall Callable<?> callable) throws Throwable {
            System.out.print(method.getName());
            System.out.println(Arrays.toString(args));

            return callable.call();
        }
    }

}
