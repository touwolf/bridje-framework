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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.el.ElEnvironment;
import org.bridje.http.UploadedFile;
import org.bridje.ioc.thls.Thls;

/**
 * Represents an input expression for a control.
 */
@XmlTransient
@XmlJavaTypeAdapter(UIFileExpressionAdapter.class)
public class UIFileExpression
{
    private static final Logger LOG = Logger.getLogger(UIFileExpression.class.getName());

    private final String expression;

    private String parameter;

    UIFileExpression(String expression)
    {
        this.expression = expression;
    }

    /**
     * Sets the value of the input expression to the web component, if the
     * expression is valid.
     *
     * @param value The value of the expression.
     */
    public void set(UploadedFile value)
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
        if (parameter == null && isValid())
        {
            parameter = getExpression().substring(2, getExpression().length() - 1);
        }
        return parameter;
    }
    
    /**
     * Gets the name of the parameter that must be send to the server.
     *
     * @return The name of the parameter that must be send to the server.
     */
    public String getParameter()
    {
        if (parameter == null && isValid())
        {
            parameter = getExpression().substring(2, getExpression().length() - 1);
        }
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

    /**
     * The value of the expression.
     *
     * @return The value of the expression.
     */
    public String getExpression()
    {
        return expression;
    }

    /**
     * Evaluates the expression and return the result casted to the given class.
     *
     * @return The result of the expression evaluation.
     */
    public UploadedFile get()
    {
        try
        {
            return Thls.get(ElEnvironment.class).get(expression, UploadedFile.class);
        }
        catch (Exception e)
        {
            LOG.log(Level.WARNING, "The expression {0} is not valid: {1}", new Object[]{expression, e.getMessage()});
        }
        return null;
    }
}
