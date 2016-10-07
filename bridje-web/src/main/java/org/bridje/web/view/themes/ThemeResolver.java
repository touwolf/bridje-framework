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

package org.bridje.web.view.themes;

/**
 * The provider of the name of the theme that will be used to render the web
 * application. This interface must be implemented by the web application.
 */
public interface ThemeResolver
{
    /**
     * Called by the web framework to get the name of the theme to be use in the
     * render of the views.
     *
     * @return The name of the theme to be use by the web application.
     */
    String findThemeName();
}
