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
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListItems
{
    @XmlAttribute(name = "list")
    private UIExpression listExpression;
    
    @XmlAttribute(name = "as")
    private String as;

    @XmlAttribute(name = "value")
    private UIExpression valueExpression;
    
    @XmlAttribute(name = "icon")
    private UIExpression iconExpression;

    @XmlValue
    private UIExpression titleExpression;

    public UIExpression getListExpression()
    {
        return listExpression;
    }

    public String getAs()
    {
        return as;
    }

    public UIExpression getValueExpression()
    {
        return valueExpression;
    }

    public UIExpression getIconExpression()
    {
        return iconExpression;
    }

    public UIExpression getTitleExpression()
    {
        return titleExpression;
    }
}
