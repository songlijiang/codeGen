package com.slj.core.processor;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({"Hello"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_6)
@AutoService(Processor.class)
public class TestProcessor extends AbstractProcessor{

    protected TestProcessor() {
        super();
    }

    @Override public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        System.out.println("HibernateAnnotationProcessor注解处理器初始化完成..............");
        super.init(processingEnv);
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        return super.getCompletions(element, annotation, member, userText);
    }

    @Override protected synchronized boolean isInitialized() {
        return super.isInitialized();
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotations.stream().forEach(System.out::println);
        roundEnv.getRootElements().stream().forEach(System.out::println);
        System.out.println(roundEnv.processingOver());
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Hello Worlds!");
        return true;
    }
}
