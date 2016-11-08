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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.web.view.widgets.Widget;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from widgets. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractWebView
{
    @XmlElements(
    {
        @XmlElement(name = "extends", type = ExtendsFrom.class),
        @XmlElement(name = "standalone", type = Standalone.class)
    })    
    private ViewDefinition definition;

    @XmlTransient
    private Widget root;

    /**
     * The root widget of this view.
     *
     * @return The root widget.
     */
    public Widget getRoot()
    {
        if(root == null && definition != null)
        {
            root = definition.findRoot();
        }
        return root;
    }

    public ViewDefinition getDefinition()
    {
        return definition;
    }
}
