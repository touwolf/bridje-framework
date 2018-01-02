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

package org.bridje.web.view.controls;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.el.ElEnvironment;
import org.bridje.ioc.thls.Thls;
import org.bridje.web.view.ViewUtils;

/**
 * Represents an input expression for a control.
 */
@XmlTransient
@XmlJavaTypeAdapter(UIInputExpressionAdapter.class)
public class UIInputExpression extends UIExpression
{
    private String parameter;

    private String fieldName;

    UIInputExpression(String expression)
    {
        super(expression);
        if (isValid())
        {
            parameter = ParamsContext.createParam(getExpression().substring(2, getExpression().length() - 1));
        }
    }

    /**
     * Sets the value of the input expression to the web component, if the
     * expression is valid.
     *
     * @param <T> The type of the expression.
     * @param value The value of the expression.
     */
    public <T> void set(T value)
    {
        if (isValid())
        {
            Thls.get(ElEnvironment.class).set(getExpression(), value);
        }
    }

    /**
     * Gets the name of the field for this input expression.
     *
     * @return The name of the field for this input expression.
     */
    public String getFieldName()
    {
        if (fieldName == null && isValid())
        {
            fieldName = getExpression().substring(2, getExpression().length() - 1);
        }
        return fieldName;
    }
    
    /**
     * Gets the name of the parameter that must be send to the server.
     *
     * @return The name of the parameter that must be send to the server.
     */
    public String getParameter()
    {
        return parameter;
    }

    /**
     * Determines if the expression is a valid input expression, or is just an
     * output expression.
     *
     * @return true the input expression is valid.
     */
    public boolean isValid()
    {
        return getExpression().startsWith("${") && getExpression().endsWith("}");
    }
}
