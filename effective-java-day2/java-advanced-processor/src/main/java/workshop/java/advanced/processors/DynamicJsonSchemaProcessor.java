package workshop.java.advanced.processors;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 */
@SupportedAnnotationTypes("workshop.java.advanced.processors.DynamicJson")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DynamicJsonSchemaProcessor extends AbstractProcessor implements javax.annotation.processing.Processor {

    public DynamicJsonSchemaProcessor() {
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(DynamicJson.class);
        for (Element element : elements) {
            if (element instanceof ExecutableElement) {
                processMethod((ExecutableElement) element);
            }
        }
        return true;
    }

    private void processMethod(ExecutableElement element) {
        try {
            String schemaClass = element.getAnnotation(DynamicJson.class).schema();
            String schemaPackage = Stream.iterate(element, Element::getEnclosingElement)
                    .filter(e -> e.getKind() == ElementKind.PACKAGE)
                    .findFirst().map(Element::toString)
                    .orElse("workshop.java.advanced.processors.schema");
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                    "Processing Json schema: " + schemaPackage + "." + schemaClass, element);

            JavaFile schemaFile = JavaFile.builder(schemaPackage, TypeSpec.classBuilder(schemaClass)
                    .addModifiers(Modifier.DEFAULT, Modifier.FINAL)
                    .addField(TypeName.BOOLEAN, "fieldX", Modifier.PRIVATE, Modifier.FINAL)
                    .build()
            ).build();

            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(schemaClass, element);
            schemaFile.writeTo(sourceFile.openWriter());
        } catch (Throwable e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Impossible to generate Json schema " + e.getMessage(), element);
        }
    }
}
