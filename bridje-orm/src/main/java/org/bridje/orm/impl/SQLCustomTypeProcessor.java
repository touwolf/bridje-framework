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
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.type.MirroredTypeException;
import org.bridje.ioc.ClassListPropertyFile;
import org.bridje.orm.SQLCustomType;

/**
 * Annotations processor for the {@link SQLCustomType} annotation.
 */
@SupportedAnnotationTypes("org.bridje.orm.SQLCustomType")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SQLCustomTypeProcessor extends ClassListPropertyFile
{
    /**
     * The web components resource file.
     */
    public static final String CUSTOM_DATATYPE_FILE = "BRIDJE-INF/orm/generated-custom-datatypes.properties";

    @Override
    public String getFileName()
    {
        return CUSTOM_DATATYPE_FILE;
    }

    @Override
    public void processElement(Element element) throws IOException
    {
        SQLCustomType annot = element.getAnnotation(SQLCustomType.class);
        String clsName = element.toString();
        String name = annot.name();
        String columnType = "";
        try
        {
            annot.columnType();
        }
        catch (MirroredTypeException e)
        {
            columnType = e.getTypeMirror().toString();
        }

        appendProperty(name, clsName + ":" + columnType);
    }
}
