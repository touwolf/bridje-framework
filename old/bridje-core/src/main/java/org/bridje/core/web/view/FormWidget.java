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

import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class FormWidget extends Widget
{
    @XmlAttribute(name = "value")
    private UIExpression valueExpression;

    @XmlAttribute(name = "placeholder")
    private String placeholder;

    public String getValue()
    {
        return valueExpression.get(String.class, "");
    }

    public UIExpression getValueExpression()
    {
        return valueExpression;
    }

    @Override
    public List<UIExpression> getInputs()
    {
        if(!valueExpression.isInputExpression())
        {
            return null;
        }
        return Arrays.asList(new UIExpression[]{valueExpression});
    }

    public void set(Object value)
    {
        valueExpression.set(value);
    }

    public String getPlaceholder()
    {
        return placeholder;
    }

    public String getFieldName()
    {
        return valueExpression.getFieldName();
    }
}
