package com.slj.core.processor;

import com.google.auto.service.AutoService;
import com.slj.core.annotation.CodeGen;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

@SupportedAnnotationTypes({"com.slj.core.annotation.CodeGen"})
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class TestProcessor extends AbstractProcessor{

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        System.out.println("TestProcessor..............");
        super.init(processingEnv);
    }


    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("process..............");
        //annotations.stream().forEach((e)->{
        //    System.out.println("annotation"+e);
        //    try {
        //        Class clazz = Class.forName(e.toString());
        //        System.out.println("class ="+clazz);
        //        System.out.println("annotation fields "+clazz+
        //            Lists.newArrayList(clazz.getFields()).stream()
        //                .map(field->field.getName()).collect(Collectors.toList()));
        //
        //    } catch (ClassNotFoundException e1) {
        //        e1.printStackTrace();
        //    }
        //});
        //roundEnv.getRootElements().stream().forEach((e)->{
        //    System.out.println("element"+e+e.getKind()+e.getSimpleName());
        //    if(e.getKind().isClass()){
        //        try {
        //            Lists.newArrayList(e.getEnclosedElements())
        //                .forEach(element->System.out.println(element));
        //
        //            System.out.println("ExecutableElement "+Lists.newArrayList(e.getEnclosedElements()).stream()
        //                .filter(element->element  instanceof ExecutableElement)
        //                .collect(Collectors.toList()));
        //            System.out.println("VariableElement "+Lists.newArrayList(e.getEnclosedElements()).stream()
        //                .filter(element->element  instanceof VariableElement)
        //                .collect(Collectors.toList()));
        //            e.getEnclosedElements().stream()
        //                .filter(element->element  instanceof VariableElement)
        //                .forEach(element->{
        //                 VariableElement element1=   ((VariableElement)element);
        //                    System.out.println(element1.asType());
        //                });
        //
        //        } catch (Exception e1){
        //            e1.printStackTrace();
        //        }
        //    }
        //});
        System.out.println(roundEnv.processingOver());

        //AnnotationSpec lombok = AnnotationSpec.builder(Data.class).build();
        roundEnv.getElementsAnnotatedWith(CodeGen.class).stream().forEach((e)->{
            System.out.println("element"+e+e.getKind()+e.getSimpleName());

            if(e.getKind().isClass() ){
                try {

                    CodeGen codeGen = e.getAnnotation(CodeGen.class);
                    String packagePath = codeGen.packagePath();
                    String projectPath = codeGen.projectPath();

                    List<FieldSpec> fieldSpecList =e.getEnclosedElements().stream()
                        .filter(element->element  instanceof VariableElement)
                        .map(element->{
                            VariableElement element1=   ((VariableElement)element);
                            return FieldSpec.builder(TypeName.get(element1.asType()),element1.getSimpleName().toString(),Modifier.PUBLIC,Modifier.FINAL,Modifier.STATIC).build();
                        }).collect(Collectors.toList());
                    String javaFileName = e.getSimpleName().toString()+"Dao";
                    Class paramClass = getParamAnnotationClass(codeGen);
                    TypeSpec typeSpec = TypeSpec.interfaceBuilder(javaFileName)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(getClassAnnotationClass(codeGen))
                        //.addFields(fieldSpecList)
                        .addMethods(fieldSpecList.stream().map(fieldSpec -> {
                            return MethodSpec.methodBuilder("findBy"+fieldSpec.name)
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .addParameter(
                                    ParameterSpec.builder(fieldSpec.type,fieldSpec.name)
                                        .addAnnotation(AnnotationSpec.builder(paramClass)
                                            .addMember("value","$S",fieldSpec.name)
                                            .build()
                                        ).build()
                                )
                                .returns(TypeName.get(e.asType()))
                                .build();
                        }).collect(Collectors.toList()))
                        .build();

                    JavaFile javaFile  =JavaFile.builder(packagePath,typeSpec).build();
                    try {
                        System.out.println(projectPath+packagePath+javaFileName+".java");
                        File target = new File(projectPath+"/"+packagePath.replaceAll("\\.","\\/")+"/"+javaFileName+".java");
                        System.out.println(target.getAbsolutePath());
                        if(!target.exists()){
                            javaFile.writeTo(new File(projectPath));
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });



        return false;
    }

    private  Class getClassAnnotationClass(CodeGen annotation) throws Exception{
        TypeMirror clazz = getClassType(annotation);
        TypeElement typeElement = asTypeElement(clazz);
        String sourceFQCN = typeElement.getQualifiedName().toString();
        return Class.forName(sourceFQCN);
    }

    private  Class getParamAnnotationClass(CodeGen annotation) throws Exception{
        TypeMirror clazz = getParamType(annotation);
        TypeElement typeElement = asTypeElement(clazz);
        String sourceFQCN = typeElement.getQualifiedName().toString();
        return Class.forName(sourceFQCN);
    }



    private  TypeMirror getClassType(CodeGen annotation) {
        try {
            annotation.daoAnnotation(); // this should throw
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen - think , think and think again??
    }
    private  TypeMirror getParamType(CodeGen annotation) {
        try {
            annotation.paramAnnotation(); // this should throw
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen - think , think and think again??
    }

    private  TypeElement asTypeElement(TypeMirror typeMirror) {
        Types TypeUtils = processingEnv.getTypeUtils();
        return (TypeElement) TypeUtils.asElement(typeMirror);
    }
}
