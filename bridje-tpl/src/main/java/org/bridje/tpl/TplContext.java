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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * This interface represents a template rendering context.
 * <p>
 * The api allows you to create many different contexts for different purposes
 * when rendering templates.
 */
public interface TplContext
{
    /**
     * Renders the specified template with the data provided to the given
     * {@link OutputStream}.
     * <p>
     * @param template The template path an name to be rendered.
     * @param data The data to be use by the template.
     * @param os The {@link OutputStream} to output the result of the rendering.
     * @throws org.bridje.tpl.TplNotFoundException
     * @throws java.io.IOException
     */
    void render(String template, Map data, OutputStream os) throws TplNotFoundException, IOException;

    /**
     * Renders the specified template with the data provided to the given
     * {@link OutputStream}.
     * <p>
     * @param template The template path an name to be rendered.
     * @param data The data to be use by the template.
     * @param file The {@link File} to output the result of the rendering.
     * @throws org.bridje.tpl.TplNotFoundException
     * @throws java.io.IOException
     */
    void render(String template, Map data, File file) throws TplNotFoundException, IOException;

    /**
     * Renders the specified template with the data provided to the given
     * {@link Writer}.
     * <p>
     * @param template The template path an name to be rendered.
     * @param data The data to be use by the template.
     * @param writer
     * @throws org.bridje.tpl.TplNotFoundException
     * @throws java.io.IOException
     */
    void render(String template, Map data, Writer writer) throws TplNotFoundException, IOException;

    /**
     * Renders the specified template with the data provided to and return the
     * result.
     * <p>
     * @param template The template path an name to be rendered.
     * @param data The data to be use by the template.
     * @return The result of the rendered template.
     * @throws org.bridje.tpl.TplNotFoundException
     * @throws java.io.IOException
     */
    String render(String template, Map data) throws TplNotFoundException, IOException;

    /**
     *  Finds out whether the specified template exists.
     * <p>
     * @param template The template path an name to be rendered.
     * @return {@literal true} if the template exists in the context,
     *         {@literal false} otherwise.
     */
    boolean exists(String template);
}
