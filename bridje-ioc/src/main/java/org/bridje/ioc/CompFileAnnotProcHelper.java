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

public abstract class CompFileAnnotProcHelper extends AbstractProcessor
{
    private Writer writer;

    private static final Logger LOG = Logger.getLogger(CompFileAnnotProcHelper.class.getName());

    public abstract String getFileName();
    
    public abstract void processElement(Element element) throws IOException;
    
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
     * This method appends class=scope to the output file.
     * <p>
     * @param clsName The full class name of the component to append
     * @param scope   The scope of the component
     * <p>
     * @throws IOException If any IO error prevents the writing.
     */
    protected void appendClass(String clsName, String scope) throws IOException
    {
        writer.append(clsName);
        writer.append("=");
        writer.append(scope);
        writer.append('\n');
        writer.flush();
    }
}
