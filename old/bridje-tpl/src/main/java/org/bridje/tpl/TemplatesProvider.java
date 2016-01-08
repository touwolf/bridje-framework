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

/**
 * A service interface that can create template loaders that will be used in the
 * tpl api.
 * <p>
 * The tpl api will use only one implementation of this interface.
 */
public interface TemplatesProvider
{
    /**
     * Creates the TemplateLoader object for the default context of the tpl api.
     * <p>
     * @return The default TemplateLoader object to be use by the api.
     */
    TemplateLoader createDefaultLoader();

    /**
     * Create a new template loader that will take the templates from the
     * specified path.
     * <p>
     * @param path The root path for the new template loader.
     * @return The TemplateLoader that will load templates that are in the
     * specified path.
     */
    TemplateLoader createLoader(String path);
}