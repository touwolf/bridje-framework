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

package org.bridje.web;

import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import org.bridje.ioc.ClassListPropertyFile;

/**
 * Annotations processor for the {@link WebComponent} annotation.
 */
@SupportedAnnotationTypes("org.bridje.web.StaticWebComponent")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class StaticWebComponentProcessor extends ClassListPropertyFile
{
    /**
     * The web components resource file.
     */
    public static final String WEB_STATIC_COMPONENTS_RESOURCE_FILE = "BRIDJE-INF/web-static/ioc-components.properties";

    @Override
    public String getFileName()
    {
        return WEB_STATIC_COMPONENTS_RESOURCE_FILE;
    }

    @Override
    public void processElement(Element element) throws IOException
    {
        String clsName = element.toString();
        String scope = "org.bridje.ioc.Application";
        appendProperty(clsName, scope);        
    }
}
