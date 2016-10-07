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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import org.bridje.web.view.widgets.Widget;

/**
 * Provides the ability to define the content of a placeholder of the parent
 * layout of the given view.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Defines
{
    @XmlAttribute
    private String name;

    @XmlAnyElement(lax = true)
    private List<Widget> widgets;

    /**
     * Gest the name of the placeholder to be defined.
     * 
     * @return The name of the placeholder.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The list of widgets that must be place in the defined placeholder.
     * 
     * @return A list of widgets.
     */
    public List<Widget> getWidgets()
    {
        if (widgets == null)
        {
            widgets = new ArrayList<>();
        }
        return widgets;
    }

    /**
     * The widget that must be place in the defined placeholder.
     * 
     * @return The widget.
     */
    public Widget getWidget()
    {
        if (widgets != null && widgets.size() > 0)
        {
            return widgets.get(0);
        }
        return null;
    }
}
