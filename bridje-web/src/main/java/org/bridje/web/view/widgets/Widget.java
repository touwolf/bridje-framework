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

package org.bridje.web.view.widgets;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Base class for all the widgets.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class Widget
{
    /**
     * Evaluates the given expression in the current ElEnviroment.
     * 
     * @param <T> The type for the result.
     * @param expression The expression to evaluate.
     * @param resultClasss The class for the result.
     * @param def The default value.
     * @return The result of the evaluation.
     */
    public static <T> T get(UIExpression expression, Class<T> resultClasss, T def)
    {
        if(expression != null)
        {
            T result = expression.get(resultClasss);
            if(result != null)
            {
                return result;
            }
        }
        return def;
    }
    
    /**
     * Gets all the UIInputExpressions in this widget.
     * 
     * @return A list of the UIInputExpressions available in this widget.
     */
    public List<UIInputExpression> inputs()
    {
        return new ArrayList<>();
    }

    /**
     * Gets all the UIEvents in this widget.
     * 
     * @return A list of the UIInputExpressions available in this widget.
     */
    public List<UIEvent> events()
    {
        return new ArrayList<>();
    }

    /**
     * Gets all child widget for this widget.
     * 
     * @return A list of all child widget.
     */
    public List<? extends Widget> childs()
    {
        return new ArrayList<>();
    }
}
