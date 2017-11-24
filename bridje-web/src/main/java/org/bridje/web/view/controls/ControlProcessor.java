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

package org.bridje.web.view.controls;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.web.view.WebLayout;
import org.bridje.web.view.WebView;

/**
 * Annotations processor for the Web Components.
 */
@SupportedAnnotationTypes("javax.xml.bind.annotation.XmlRootElement")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@XmlTransient
public class ControlProcessor extends AbstractProcessor
{
    private Writer writer;

    private static final Logger LOG = Logger.getLogger(ControlProcessor.class.getName());

    /**
     * The bridje controls resources file path.
     */
    public static final String CONTROLS_RESOURCE_FILE = "BRIDJE-INF/web/controls.properties";

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
            FileObject fobj = filer.createResource(StandardLocation.CLASS_OUTPUT, "", CONTROLS_RESOURCE_FILE);
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
                //Find all XmlRootElement marked classes
                Set<? extends Element> ann = roundEnv.getElementsAnnotatedWith(typeElement);
                for (Element element : ann)
                {
                    if (element.getKind() == ElementKind.CLASS)
                    {
                        if(isControl((TypeElement)element))
                        {
                            String clsName = element.toString();
                            appendClass(clsName, "");
                        }
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

    private void appendClass(String clsName, String val) throws IOException
    {
        writer.append(clsName);
        writer.append("=");
        writer.append(val);
        writer.append('\n');
        writer.flush();
    }

    private boolean isControl(TypeElement element)
    {
        List<? extends TypeMirror> interfaces = element.getInterfaces();
        for (TypeMirror ifc : interfaces)
        {
            if(ifc.toString().equalsIgnoreCase(WebView.class.getName()))
            {
                return true;
            }
            if(ifc.toString().equalsIgnoreCase(WebLayout.class.getName()))
            {
                return true;
            }
        }
        return false;
    }
}
