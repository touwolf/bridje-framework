/*
 * Copyright 2015 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.orm.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("org.bridje.orm.Generate")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EntityProcessor extends AbstractProcessor
{
    private static final Logger LOG = Logger.getLogger(EntityProcessor.class.getName());

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        //Creating necesary objects for annotations procesing.
        super.init(processingEnv);
        Messager messager = processingEnv.getMessager();
        try
        {
            filer = processingEnv.getFiler();
        }
        catch (Exception e)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        Messager messager = processingEnv.getMessager();
        try
        {
            for (TypeElement typeElement : annotations)
            {
                Set<? extends Element> ann = roundEnv.getElementsAnnotatedWith(typeElement);
                for (Element element : ann)
                {
                    if (element.getKind() == ElementKind.CLASS)
                    {
                        generateClass(element);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
            LOG.severe(ex.getMessage());
        }
        return true;
    }

    private void generateClass(Element element) throws IOException
    {
        String entityClassName = element.getSimpleName().toString();
        String className = entityClassName + "_";
        String fullClassName = element.toString() + "_";
        String classPack = findPackage(fullClassName);

        JavaFileObject fo = filer.createSourceFile(fullClassName);
        try(Writer writer = fo.openWriter())
        {
            
            ClassWriter cw = new ClassWriter(writer);
            //Package
            cw.emptyLine();
            cw.classPackage(classPack);
            //Imports
            cw.importClass("javax.annotation.Generated");
            cw.importClass("org.bridje.orm.Table");
            cw.importClass("org.bridje.orm.Column");
            //Class
            cw.emptyLine();
            cw.annotate("Generated(value = \"bridje-orm\")");
            cw.publicAccess().finalElement().classDec(className).extendsFrom("Table<" + entityClassName + ">");
            cw.begin();
                //Table Field
                cw.publicAccess().staticElement().finalElement().fieldDec(className, "table", "new " + className + "()");
                //Column Fields
                element.getEnclosedElements().stream()
                        .filter((e) -> e.getKind() == ElementKind.FIELD)
                        .forEach((e) -> writeField(cw, e, element));
                //Constructor
                cw.privateAccess().contructorDec(className);
                cw.begin();
                    cw.codeLine("super(" + cw.dotClass(entityClassName) + ")");
                cw.end();
            //End
            cw.end();
            writer.flush();
        }
    }

    private String findPackage(String fullClsName)
    {
        return fullClsName.substring(0, fullClsName.lastIndexOf("."));
    }

    private void writeField(ClassWriter cw, Element fieldEl, Element classEl)
    {
        Messager messager = processingEnv.getMessager();
        try
        {
            String entityName = classEl.getSimpleName().toString();
            String name = fieldEl.getSimpleName().toString();
            String fieldType = cw.removeJavaLangPack(fieldEl.asType().toString());
            String type = cw.createGenericType("Column", entityName, fieldType);
            String value = cw.newObjStatement("Column<>",
                    entityName + "_.table", 
                    cw.stringLiteral(name),
                    cw.dotClass(fieldType));
            cw.publicAccess().staticElement().finalElement().fieldDec(type, name, value);
        }
        catch (Exception e)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            LOG.severe(e.getMessage());
        }
    }
}
