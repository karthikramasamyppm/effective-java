package workshop.java.advanced.processors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 *
 */
public class JsonProcessing {

    public static <T extends Json<T>> T parse(String json, Class<T> schema) {
        try {
            return schema.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("class " + schema.getName() + " is not proper schema");
        }
    }

    public static class Json<NODE> {

        protected final String fieldName;

        public Json(String fieldName) {
            this.fieldName = fieldName;
        }

        // int
        public Json get(String field) {
            return new Json(field);
        }

        public int asInt() {
            return 0;
        }

        public OptionalInt optionalInt() {
            return OptionalInt.empty();
        }

        public int optionalInt(int defaultInt) {
            return defaultInt;
        }

        public int optionalInt(IntSupplier defaultSupplier) {
            return defaultSupplier.getAsInt();
        }

        // long
        // boolean
        // double
        // BigDecimal
        // LocalDate
        // LocalTime
        // ZonedDateTime
        // OffsetDateTime

        // object
        public <T> T asObject(Function<NODE, T> mapper) {
            return null;
        }

        public <T> Optional<T> optionalObject(Function<NODE, T> mapper) {
            return Optional.empty();
        }

        public <T> T optionalObject(Function<NODE, T> mapper, Supplier<T> defaultSupplier) {
            return defaultSupplier.get();
        }

        // array
        public <T> List<T> asList(Function<NODE, T> elementMapper) {
            return Collections.emptyList();
        }

        public <K, V> Map<K, V> asMap(Function<NODE, K> keyMapper, Function<NODE, V> valueMapper) {
            return Collections.emptyMap();
        }

        public <K, V, M extends Map<K, V>> M asMap(Supplier<M> mapSupplier, Function<NODE, K> keyMapper, Function<NODE, V> valueMapper) {
            return mapSupplier.get();
        }

        public <T, C extends Collection<T>> C asCollection(Supplier<C> collection, Function<NODE, T> elementMapper) {
            return collection.get();
        }

        public <C> C asCollection(Supplier<C> collectionSupplier, BiConsumer<C, NODE> elementConsumer) {
            return collectionSupplier.get();
        }

        public <C, F> F asCollection(Supplier<C> collectionSupplier, BiConsumer<C, NODE> elementConsumer, Function<C, F> fin) {
            return fin.apply(collectionSupplier.get());
        }

        //////////////////////////////////


        public LocalDate asLocalDate() {
            return null;
        }

        public LocalDate asLocalDate(DateTimeFormatter formatter) {
            return null;
        }

        public LocalDate asLocalDate(String format) {
            return asLocalDate(DateTimeFormatter.ofPattern(format));
        }

        public String asString() {
            return null;
        }
    }
}
