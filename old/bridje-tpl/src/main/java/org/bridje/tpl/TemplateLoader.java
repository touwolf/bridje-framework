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

package org.bridje.tpl;

import java.io.IOException;
import java.io.InputStream;

/**
 * Defines a templates loader.
 * <p>
 * The objects implementing this interface are responsible for loading templates
 * on {@link InputStream} objects from their storage and provides it to the tpl api.
 */
public interface TemplateLoader
{
    /**
     * 
     * @param path
     * @return 
     */
    Object findTemplate(String path);
    
    /**
     * Gets an InputStream to the template specified by the path parameter.
     * <p>
     * @param template 
     * @return An InputStream witch allows the api to read the content of the
     * template.
     * @throws java.io.IOException
     */
    InputStream loadTemplate(Object template) throws IOException;
}
