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
/**
 * An interface that every template engine must implement to provide his
 * functionality in the tpl api.
 */
public interface TplEngine
{
    /**
     * The extensions used by the templates of this template engine.
     * <p>
     * @return A String representing the extension that the files that this
     * engine can render must have. The name must not have the dot (".")
     * character.(Eg: "tpl" NOT ".tpl")
     */
    String getExtension();

    /**
     * Creates a template engine context for the specified loader. The tpl api
     * will use this context to render templates that match the extension of
     * this engine.
     * <p>
     * @param loader The loader from witch the result context may load the
     * templates.
     * @return An TplEngineContext object that can render templates of this
     * engine.
     */
    TplEngineContext createContext(TemplateLoader loader);
}
