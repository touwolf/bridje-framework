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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.ioc.Ioc;
import org.bridje.web.view.widgets.Widget;

/**
 * Defines that the view will extend from the given layout.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtendsFrom extends ViewDefinition
{
    @XmlAttribute
    private String layout;

    @XmlElements(
            {
                @XmlElement(name = "define", type = Defines.class)
            })
    private List<Defines> defines;

    @XmlTransient
    private Map<String, Defines> definesMap;

    /**
     * Gets all the placeholder definitions for this view.
     *
     * @return A map with the name of the placeholder an the corresponding
     * Defines object.
     */
    public Map<String, Defines> getDefinesMap()
    {
        if (definesMap == null)
        {
            initDefinesMap();
        }
        return definesMap;
    }

    /**
     * Gets the list of defines for this view.
     *
     * @return A list of Defines objects.
     */
    public List<Defines> getDefines()
    {
        return defines;
    }

    /**
     * The name of the parent layout.
     * 
     * @return An string with the name of the parent layout.
     */
    public String getLayout()
    {
        return layout;
    }

    @Override
    public Widget findRoot()
    {
        WebLayoutManager layoutManag = Ioc.context().find(WebLayoutManager.class);
        WebLayout webLayout = layoutManag.loadLayout(layout);
        if (webLayout != null)
        {
            Widget widget = webLayout.getRoot();
            widget.override(getDefinesMap());
            return widget;
        }
        return null;
    }

    private void initDefinesMap()
    {
        definesMap = new HashMap<>();
        for (Defines define : defines)
        {
            definesMap.put(define.getName(), define);
        }
    }

}
