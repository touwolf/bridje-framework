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

package org.bridje.core.tpl;

import java.io.InputStream;

/**
 * Defines a templates loader.
 *
 * The objects implementing this interface are resposable for loading templates
 * InputStreams from their storage and provide it to the tpl api.
 */
@FunctionalInterface
public interface TemplateLoader
{
    /**
     * Gets an InputStream to the template specified by the path param.
     *
     * @param path The path of the template to be loaded.
     * @return An InputStream witch allows the api to read the content of the
     * template.
     */
    InputStream loadTemplate(String path);
}
