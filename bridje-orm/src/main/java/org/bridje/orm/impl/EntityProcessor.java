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
import java.util.Set;
import java.util.logging.Logger;
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
        JavaFileObject fo = filer.createSourceFile(element.toString() + "_");
        try(Writer writer = fo.openWriter())
        {
            writePackage(writer, element);
            //Imports
            writeClassDec(writer, element);
            //Code
            writeTableField(writer, element);
            writeFields(writer, element);
            writeDefConstructor(writer, element);
            //Finish Code
            writeClassDecClose(writer);
            writer.flush();
        }
    }

    private String findPackage(String fullClsName)
    {
        return fullClsName.substring(0, fullClsName.lastIndexOf("."));
    }

    private void writePackage(Writer writer, Element element) throws IOException
    {
        writer.append("package ");
        writer.append(findPackage(element.toString()));
        writer.append(";\n");
    }

    private void writeClassDec(Writer writer, Element element) throws IOException
    {
        writer.append("\n@javax.annotation.Generated(value = \"bridje-orm\")");
        writer.append("\npublic class ");
        writer.append(element.getSimpleName() + "_");
        writer.append(" extends");
        writer.append(" org.bridje.orm.EntityTable<");
        writer.append(element.getSimpleName());
        writer.append(">");
        writer.append("\n{");
    }

    private void writeClassDecClose(Writer writer) throws IOException
    {
        writer.append("\n}");
    }

    private void writeDefConstructor(Writer writer, Element element) throws IOException
    {
        writer.append("\n    ");
        writer.append(element.getSimpleName() + "_()");
        writer.append("\n    {");
        writer.append("\n        super(");
        writer.append(element.getSimpleName());
        writer.append(".class);");
        writer.append("\n    }");
    }

    private void writeFields(Writer writer, Element element)
    {
        element.getEnclosedElements().stream()
                .filter((e) -> e.getKind() == ElementKind.FIELD)
                .forEach((e) -> writeField(writer, e, element));
    }
    
    private void writeField(Writer writer, Element element, Element parent)
    {
        Messager messager = processingEnv.getMessager();
        try
        {
            String type = element.asType().toString();
            String name = element.getSimpleName().toString();
            writer.append("\n    public static final ");
            writer.append("org.bridje.orm.EntityColumn<");
            writer.append(parent.getSimpleName().toString());
            writer.append(", ");
            writer.append(type);
            writer.append("> ");
            writer.append(name);
            writer.append(" = new org.bridje.orm.EntityColumn<>(");
            writer.append(parent.getSimpleName().toString());
            writer.append("_.table, ");
            writer.append("\"");
            writer.append(name);
            writer.append("\", ");
            writer.append(type);
            writer.append(".class");
            writer.append(")");
            writer.append(";\n");
        }
        catch (Exception e)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            LOG.severe(e.getMessage());
        }
    }
    
    private void writeTableField(Writer writer, Element element) throws IOException
    {
        writer.append("\n    public static final ");
        writer.append(element.getSimpleName() + "_");
        writer.append(" table = new ");
        writer.append(element.getSimpleName() + "_();\n");
    }
}
