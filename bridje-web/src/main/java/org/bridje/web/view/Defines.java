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
import org.bridje.web.view.controls.Control;

/**
 * Provides the ability to define the content of a placeholder of the parent
 * layout of the given view.
 */
public interface Defines
{
    /**
     * Gest the name of the placeholder to be defined.
     * 
     * @return The name of the placeholder.
     */
    String getName();

    /**
     * The list of controls that must be place in the defined placeholder.
     * 
     * @return A list of controls.
     */
    List<Control> getControls();

    /**
     * The control that must be place in the defined placeholder.
     * 
     * @return The control.
     */
    Control getControl();
}
