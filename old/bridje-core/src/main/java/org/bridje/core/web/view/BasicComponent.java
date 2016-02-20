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

package org.bridje.core.web.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class BasicComponent extends Component
{
    @XmlAttribute
    private String id;

    @XmlAttribute(name = "class")
    private UIExpression classExpression;

    @XmlAttribute(name = "visible")
    private UIExpression visibleExpression;

    public String getId()
    {
        return id;
    }
    
    public String getStyleClass()
    {
        return classExpression.get(String.class, "");
    }

    public UIExpression getClassExpression()
    {
        return classExpression;
    }

    public boolean getVisible()
    {
        return visibleExpression.get(Boolean.class, true);
    }

    public UIExpression getVisibleExpression()
    {
        return visibleExpression;
    }

    public boolean hasStyleClass(String clsName)
    {
        String styleClass = getStyleClass();
        String[] split = styleClass.split(" ");
        for (String split1 : split)
        {
            if(split1.equals(clsName))
            {
                return true;
            }
        }
        return false;
    }
}
