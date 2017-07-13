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
import org.bridje.ioc.thls.Thls;

/**
 * Represents an expression that can be use in a view to pull a value from the
 * model during a view render.
 */
@XmlTransient
@XmlJavaTypeAdapter(UIExpressionAdapter.class)
public class UIExpression
{
    private static final Logger LOG = Logger.getLogger(UIExpression.class.getName());

    private final String expression;

    UIExpression(String expression)
    {
        this.expression = expression;
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
     * @param <T>          The type for the result of this expression.
     * @param resultClasss The class for the result of this expression.
     *
     * @return The result of the expression evaluation.
     */
    public <T> T get(Class<T> resultClasss)
    {
        try
        {
            return Thls.get(ElEnvironment.class).get(expression, resultClasss);
        }
        catch (Exception e)
        {
            LOG.log(Level.WARNING, "The expression {0} is not valid: {1}", new Object[]{expression, e.getMessage()});
        }
        return null;
    }

    /**
     * Evaluates the expression and return the result casted to the given class.
     *
     * @return The result of the expression evaluation.
     */
    public String getAsString()
    {
        return get(String.class);
    }
}
