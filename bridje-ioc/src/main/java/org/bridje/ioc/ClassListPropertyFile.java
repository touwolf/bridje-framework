/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.ioc;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Base class for the annotations processors that handle components declaration files.
 */
public abstract class ClassListPropertyFile extends AbstractProcessor
{
    private Writer writer;

    private static final Logger LOG = Logger.getLogger(ClassListPropertyFile.class.getName());

    /**
     * Gets the name for the file that will be written by this annotation processor.
     * 
     * @return The name of the file for this annotation processor.
     */
    public abstract String getFileName();

    /**
     * This method will be called for each component class found by this processor.
     * the implementation of this method must call the appendClass method to write
     * the component reference to de file.
     * 
     * @param element The element representing the current component class.
     * @throws IOException If the component cannot be written to the file.
     */
    public abstract void processElement(Element element) throws IOException;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        //Creating necessary objects for annotations processing.
        super.init(processingEnv);
        Messager messager = processingEnv.getMessager();
        try
        {
            Filer filer = processingEnv.getFiler();
            //Creating output file
            FileObject fobj = filer.createResource(StandardLocation.CLASS_OUTPUT, "", getFileName());
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
                //Find all @Component marked classes
                Set<? extends Element> ann = roundEnv.getElementsAnnotatedWith(typeElement);
                for (Element element : ann)
                {
                    if (element.getKind() == ElementKind.CLASS)
                    {
                        processElement(element);
                    }
                }
            }
        }
        catch (IOException ex)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
            LOG.severe(ex.getMessage());
        }
        return true;
    }

    /**
     * This method appends key=value to the output file.
     * <p>
     * @param key   The full class name of the component to append
     * @param value The scope of the component
     * <p>
     * @throws IOException If any IO error prevents the writing.
     */
    protected void appendProperty(String key, String value) throws IOException
    {
        writer.append(key);
        writer.append("=");
        writer.append(value);
        writer.append('\n');
        writer.flush();
    }
}
