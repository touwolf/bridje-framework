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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpReqParam;
import org.bridje.web.view.Defines;

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
    
    public static <T> void set(UIInputExpression expression, HttpReqParam param)
    {
        if(param != null)
        {
            if(expression != null && expression.isValid())
            {
                expression.set(param);
            }
        }
    }
    
    /**
     * Gets all the UIInputExpressions in this widget.
     * 
     * @return A list of the UIInputExpressions available in this widget.
     */
    public List<UIInputExpression> inputs()
    {
        return Collections.EMPTY_LIST;
    }

    /**
     * Gets all the UIEvents in this widget.
     * 
     * @return A list of the UIInputExpressions available in this widget.
     */
    public List<UIEvent> events()
    {
        return Collections.EMPTY_LIST;
    }

    /**
     * Gets all child widget for this widget.
     * 
     * @return A list of all child widget.
     */
    public List<? extends Widget> childs()
    {
        return Collections.EMPTY_LIST;
    }
    
    public List<String> resources()
    {
        return Collections.EMPTY_LIST;
    }

    /**
     * 
     * @param req 
     */
    public void readInput(HttpBridletRequest req)
    {
        inputs().stream()
                .forEach((input) -> set(input, req.getPostParameter(input.getParameter())));
        childs().stream()
                .forEach((widget) -> widget.readInput(req));
    }

    public void override(Map<String, Defines> definesMap)
    {
        doOverride(definesMap);
        childs().stream()
                .forEach((widget) -> widget.override(definesMap));        
    }

    public static List<Widget> doOverride(List<Widget> childrens, Map<String, Defines> definesMap)
    {
        List<Widget> result = new ArrayList<>();
        
        for (Widget widget : childrens)
        {
            if(widget instanceof WidgetPlaceHolder)
            {
                WidgetPlaceHolder ph = (WidgetPlaceHolder)widget;
                Defines def = definesMap.get(ph.getName());
                if(def != null && def.getComponents() != null)
                {
                    result.addAll(def.getComponents());
                }
            }
            else
            {
                result.add(widget);
            }
        }
        
        return result;
    }
    
    public static Widget doOverride(Widget child, Map<String, Defines> definesMap)
    {
        Widget result = null;
        
        if(child instanceof WidgetPlaceHolder)
        {
            WidgetPlaceHolder ph = (WidgetPlaceHolder)child;
            Defines def = definesMap.get(ph.getName());
            if(def != null && def.getComponent() != null)
            {
                result = def.getComponent();
            }
        }
        else
        {
            result = child;
        }
        
        return result;
    }
    
    public abstract void doOverride(Map<String, Defines> definesMap);
}
