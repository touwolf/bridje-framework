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

import org.bridje.web.view.controls.Control;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
public interface AbstractView
{
    /**
     * The root control of this view.
     *
     * @return The root control.
     */
    Control getRoot();

    /**
     * Gets the default theme associated with the roor control of the view.
     * 
     * @return The default theme for the root control of the view.
     */
    String getDefaultTheme();

    /**
     * Gets the view definition control for this view.
     * 
     * @return The view definition control, standalone or extends.
     */
    ViewDefinition getDefinition();
}
