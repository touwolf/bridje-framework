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
 * An interface that must be implemented by the component that will retrieve the
 * content of the given resources.
 */
@FunctionalInterface
public interface ResourceRenderProvider
{
    /**
     * Gets the content of the given resource.
     * 
     * @param theme    The name of the current theme.
     * @param resource The resource name.
     * @return The content of the resource.
     */
    String renderResource(String theme, String resource);
}
