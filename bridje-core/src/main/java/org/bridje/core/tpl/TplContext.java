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

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * This interface represents a template rendering context. The tpl api allows
 * you to create many diferent contexts for diferent propouse when rendering
 * templates.
 */
public interface TplContext
{
    /**
     * Renders the especified template with the data provided to the gived
     * OutputStream.
     *
     * @param template The template path an name to be rendered.
     * @param data The data to be use by the template.
     * @param os The OutputStream to output the result of the rendering.
     */
    void render(String template, Map data, OutputStream os);

    /**
     * Renders the especified template with the data provided to the gived
     * Writer.
     *
     * @param template The template path an name to be rendered.
     * @param data The data to be use by the template.
     * @param writer
     */
    void render(String template, Map data, Writer writer);

    /**
     * Renders the especified template with the data provided to and return the
     * result.
     *
     * @param template The template path an name to be rendered.
     * @param data The data to be use by the template.
     * @return The result of the rendered template.
     */
    String render(String template, Map data);
}
