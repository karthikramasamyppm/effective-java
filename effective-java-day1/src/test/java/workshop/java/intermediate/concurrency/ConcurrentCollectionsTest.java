package workshop.java.intermediate.concurrency;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ConcurrentCollectionsTest {

    // thread unsafe
    // fail safe iteration
    @Test
    public void collections() throws Exception {
        List<String> team = new ArrayList<>(Arrays.asList("Michał", "Przemek", "Paweł", "Tomasz"));

        assertThatExceptionOfType(ConcurrentModificationException.class)
                .isThrownBy(() -> {
                    for (String name : team) {
                        team.remove(name);
                    }
                });
    }

    // thread safe
    // week consistent iteration
    // no semantic size limits
    // not accept nulls
    @Test
    public void threadSafe() throws Exception {
        // an optionally bounded FIFO blocking queue backed by linked nodes
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        Map<String, Object> map = new ConcurrentHashMap<>();
        Set<String> set = Collections.newSetFromMap(new ConcurrentHashMap<>());

        // queue.add(null);
        // map.put(null, null);
        // set.add(null);
    }

    @Test
    public void iterationAndModification() throws Exception {
        Queue<String> team = new ConcurrentLinkedQueue<>(
                Arrays.asList("Michał", "Przemek", "Paweł", "Tomasz")
        );

        for (String name : team) {
            team.remove(name);
        }

        assertThat(team).isEmpty();

    }

    // starts with size 0
    // thread safe without locking / synchronisation
    // iterators never throw ConcurrentModificationException
    // closely modification
    @Test
    public void copyOnWriteList() throws Exception {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        List<String> view = new CopyOnWriteArrayList<>(list);

        CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
    }

    // thread safe
    // week consistent iteration
    // semantic size limits -> wait on add
    // wait on get if empty
    // not accept nulls
    @Test
    public void blocking() throws Exception {
        // an optionally bounded FIFO blocking queue backed by linked nodes
        new LinkedBlockingDeque<>();
        // a bounded FIFO blocking queue backed by an array
        new ArrayBlockingQueue<String>(1_000);
        // an unbounded blocking priority queue backed by a heap
        new PriorityBlockingQueue<String>();
    }

    // a time-based scheduling queue backed by a heap
    // element can only be taken when its delay has expired
    @Test
    public void delayQueue() throws Exception {
        DelayQueue<Delayed> delayeds = new DelayQueue<>();

    }

    @Test
    public void rendezvousQueues() throws Exception {
        // producers wait for consumers to poll elements
        // acts as an empty collection for all Collection methods
        // remove() and poll() will return null
        // contains return false, etc
        new SynchronousQueue<>();

        // producers may wait for consumers to receive elements [method transfer(E)]
        // producers may offer element without waiting for receipt [method put(E)]
        new LinkedTransferQueue<String>();
    }
}
