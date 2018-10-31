package workshop.java.advanced.processors;

import com.squareup.javapoet.*;
import org.junit.Test;
import workshop.java.advanced.dsl.DomainObjects.CallofDocument;
import workshop.java.advanced.dsl.DomainObjects.DailyDemand;
import workshop.java.advanced.dsl.DomainObjects.DateIncrementer;
import workshop.java.advanced.dsl.DomainObjects.ProductDemand;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static workshop.java.advanced.dsl.$EntryTest.*;

/**
 *
 */
public class CodeGeneratorTest {

    private static class SchemaObject {

        private static final ClassName JSON = ClassName.get("workshop.java.advanced.processors.JsonProcessing", "Json");

        private String fieldName;
        private String className;
        private Map<String, SchemaObject> children;

        public SchemaObject(String fieldName, String className, List<SchemaObject> children) {
            this.fieldName = fieldName;
            this.className = className;
            this.children = children.stream().collect(
                    Collectors.toMap(o -> o.fieldName, Function.identity()));
        }

        public SchemaObject(String fieldName, String className) {
            this.fieldName = fieldName;
            this.className = className;
            this.children = new HashMap<>();
        }

        public SchemaObject field(String field) {
            if (!children.containsKey(field)) {
                SchemaObject child = new SchemaObject(field,
                        field.substring(0, 1).toUpperCase() + field.substring(1));
                children.put(field, child);
                return child;
            } else {
                return children.get(field);
            }
        }

        public FieldSpec fieldSpec() {
            ClassName fieldClassName = ClassName.get("", className);
            return FieldSpec.builder(fieldClassName, fieldName, Modifier.PUBLIC, Modifier.FINAL)
                    .initializer("new $T($S)", fieldClassName, fieldName).build();
        }

        public TypeSpec typeSpec() {
            return TypeSpec.classBuilder(className)
                    .superclass(ParameterizedTypeName.get(JSON, ClassName.bestGuess(className)))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PRIVATE)
                            .addParameter(String.class, "fieldName")
                            .addStatement("super($N)", "fieldName")
                            .build())
                    .addFields(children.values().stream()
                            .map(SchemaObject::fieldSpec)
                            .collect(Collectors.toList()))
                    .addTypes(children.values().stream()
                            .map(SchemaObject::typeSpec)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Test
    public void classWithFields() throws Exception {
        String schemaPackage = "workshop.java.advanced.processors";
        String schemaClass = "CallofDocumentSchema";

        SchemaObject root = new SchemaObject("root", schemaClass, Arrays.asList(
                new SchemaObject("created", "Created", Collections.emptyList()),
                new SchemaObject("products", "Product", Arrays.asList(
                        new SchemaObject("refNo", "RefNo", Collections.emptyList()),
                        new SchemaObject("order", "Order", Collections.emptyList())
                ))
        ));

        JavaFile schemaFile = JavaFile.builder(schemaPackage, root.typeSpec()).build();
        schemaFile.writeTo(System.out);
    }


    /*
          CallofDocument document = json.asObject(doc -> new CallofDocument(
                    doc.get("created").asLocalDate(),
                    doc.get("products").asList(product -> {
                        DateIncrementer date = new DateIncrementer(doc.get("created").asLocalDate());
                        return new ProductDemand(
                                product.get("refNo").asString(),
                                product.get("order").asMap(TreeMap::new,
                                        daily -> date.getAndIncrement(),
                                        daily -> new DailyDemand(
                                                date.last(), (long) daily.asInt(), DeliveryOn.START_OF_DAY
                                        ))
                        );
                    })
          ));
    */
    @Test
    public void tryExampleSchema() throws Exception {
        CallofDocumentSchema json = JsonProcessing.parse(text, CallofDocumentSchema.class);
        CallofDocument document = json.asObject(doc -> new CallofDocument(
                doc.created.asLocalDate(),
                doc.products.asList(product -> {
                    DateIncrementer date = new DateIncrementer(doc.get("created").asLocalDate());
                    return new ProductDemand(
                            product.refNo.asString(),
                            product.order.asMap(TreeMap::new,
                                    daily -> date.getAndIncrement(),
                                    daily -> new DailyDemand(
                                            date.last(), level(daily.asString()), deliveryOn(daily.asString())
                                    ))
                    );
                })
        ));
    }
}
