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
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.bridje.orm.Entity;

/**
 * Annotations processor for the {@link Entity} annotation.
 */
@SupportedAnnotationTypes("org.bridje.orm.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EntityProcessor extends AbstractProcessor
{
    private Writer writer;

    private static final Logger LOG = Logger.getLogger(EntityProcessor.class.getName());

    /**
     * The bridje orm entitys resources file path.
     */
    public static final String ENTITYS_RESOURCE_FILE = "BRIDJE-INF/orm/entitys.properties";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        //Creating necesary objects for annotations procesing.
        super.init(processingEnv);
        Messager messager = processingEnv.getMessager();
        try
        {
            Filer filer = processingEnv.getFiler();
            //Creating output file
            FileObject fobj = filer.createResource(StandardLocation.CLASS_OUTPUT, "", ENTITYS_RESOURCE_FILE);
            writer = fobj.openWriter();
        }
        catch (IOException e)
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
                //Find all @Entity marked classes
                Set<? extends Element> ann = roundEnv.getElementsAnnotatedWith(typeElement);
                for (Element element : ann)
                {
                    if (element.getKind() == ElementKind.CLASS)
                    {
                        //Get the @Entity annotation for the current element.
                        Entity annot = element.getAnnotation(Entity.class);
                        String clsName = element.toString();
                        String table = annot.table();
                        appendClass(clsName, table);
                    }
                }
            }
        }
        catch (IOException ex)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
            LOG.severe(ex.getMessage());
        }
        return false;
    }

    private void appendClass(String clsName, String table) throws IOException
    {
        writer.append(clsName);
        writer.append("=");
        writer.append(table);
        writer.append('\n');
        writer.flush();
    }
}
