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

package org.bridje.web.view;

import java.util.List;
import java.util.Map;

/**
 * Defines that the view will extend from the given layout.
 */
public interface ExtendsFrom extends ViewDefinition
{
    /**
     * Gets all the placeholder definitions for this view.
     *
     * @return A map with the name of the placeholder an the corresponding
     * Defines object.
     */
    Map<String, Defines> getDefinesMap();

    /**
     * Gets the list of defines for this view.
     *
     * @return A list of Defines objects.
     */
    List<Defines> getDefines();

    /**
     * The name of the parent layout.
     * 
     * @return An string with the name of the parent layout.
     */
    String getLayout();
}
