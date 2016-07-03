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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Base class for all the web components.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class WebComponent
{
    @XmlAttribute
    private String id;

    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    @XmlAttribute(name = "class")
    private UIExpression styleClass;

    /**
     * Gets the component`s id.
     * 
     * @return The component´s id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the component`s id.
     * 
     * @param id The component´s id.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Gets the html style class for this component.
     * 
     * @return The space separated html style classes.
     */
    public String getStyleClass()
    {
        return get(styleClass, String.class, "");
    }
    
    /**
     * Evals the given expression in the current ElEnviroment.
     * 
     * @param <T> The type for the result.
     * @param expression The expression to evaluate.
     * @param resultClasss The class for the result.
     * @param def The default value.
     * @return The result of the evaluation.
     */
    protected <T> T get(UIExpression expression, Class<T> resultClasss, T def)
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
     * Gets all the UIInputExpressions in this component.
     * 
     * @return A list of the UIInputExpressions available in this component.
     */
    public List<UIInputExpression> inputs()
    {
        return new ArrayList<>();
    }

    /**
     * Gets all the UIEvents in this component.
     * 
     * @return A list of the UIInputExpressions available in this component.
     */
    public List<UIEvent> events()
    {
        return new ArrayList<>();
    }

    /**
     * Gets all child components for this component.
     * 
     * @return A list of all child components.
     */
    public List<? extends WebComponent> childs()
    {
        return new ArrayList<>();
    }
}
