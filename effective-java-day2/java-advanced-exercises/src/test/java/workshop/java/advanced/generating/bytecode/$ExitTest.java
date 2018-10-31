package workshop.java.advanced.generating.bytecode;

import lombok.Value;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import workshop.java.advanced.dsl.DomainObjects.CallofDocument;
import workshop.java.advanced.dsl.DomainObjects.ProductDemand;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 *
 */
public class $ExitTest {

    // Task ZERO.
    // let maven pull all documentation and sources for libraries


    // Task 1.
    // create dynamically lazy loading value of any type
    // proper value should be loaded by first usage of any method
    //

    @Test
    public void lazyLoad() throws Exception {
        CallofDocument doc = lazy(this::parseDoc);
        // doc not initialized
        doc.getCreated(); // doc initialized now
    }

    private <T> T lazy(Supplier<T> loader) {
        // TODO create lazy proxy
        return null;
    }

    private CallofDocument parseDoc() {
        return null; // provide some example
    }


    // Task 2.
    // create aspect / proxy collecting any of exception thrown from original class
    // exceptions should be swallowed
    //

    @Test
    public void swallowExceptionsAspect() throws Exception {
        ProductDemand demand = swallowing(parseDoc().getDemands().get(0));

        // probably will throw IllegalStateException
        demand.fancyBusinessLogic();

        List<Throwable> exceptions = exceptions(demand);
        Assertions.assertThat(exceptions).isNotEmpty();
    }

    private <T> T swallowing(T throwing) {
        // TODO create swallowing proxy
        return null;
    }

    private List<Throwable> exceptions(Object swallowing) {
        // TODO return exceptions thrown inside swallowing proxy
        return Collections.emptyList();
    }


    // Task 3.
    // once more
    // create aspect / proxy collecting any of exception thrown from original class
    // exceptions should be swallowed
    // try to propagate that swallowing behaviour to data returned from proxy instance
    // all exceptions thrown inside graph of swallowing proxies should be accessible by root proxy
    //

    @Test
    public void swallowExceptionsPropagatingAspect() throws Exception {
        CallofDocument doc = deepSwallowing(parseDoc());

        // probably will throw IllegalStateException
        doc.getDemands().get(1).fancyBusinessLogic();

        List<Throwable> exceptions = exceptions(doc);
        Assertions.assertThat(exceptions).isNotEmpty();
    }

    private <T> T deepSwallowing(T throwing) {
        // TODO create swallowing proxy
        return null;
    }


    // Task 4.
    // create proxy for entity class recording usage of getter methods
    // to allow define Hibernate / JPA mappings outside of entity class
    // but using entity methods calls instead of textual property names
    //

    @Value
    private static class Entity {
        int entityId;
        int entityAge;
    }

    @Test
    public void ormMappings() throws Exception {
        Entity entity = mappingSubject(Entity.class);

        addColumnMapping(entity.getEntityId(), "id");
        addColumnMapping(entity.getEntityAge(), "age");
    }

    private <T> T mappingSubject(Class<T> entity) {
        // TODO create mapping subject
        return null;
    }

    private void addColumnMapping(Object entityProperty, String columnName) {
    }

    // Task 5.
    // if you fills inspired, we can implement some other funky examples
    //

}
