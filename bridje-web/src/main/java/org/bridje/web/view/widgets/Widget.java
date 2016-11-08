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
     * Evaluates the given expression in the current ElEnvironment.
     *
     * @param <T> The type for the result.
     * @param expression The expression to evaluate.
     * @param resultClass The class for the result.
     * @param def The default value.
     * @return The result of the evaluation.
     */
    public static <T> T get(UIExpression expression, Class<T> resultClass, T def)
    {
        if (expression != null)
        {
            T result = expression.get(resultClass);
            if (result != null)
            {
                return result;
            }
        }
        return def;
    }

    /**
     * Sets the value of the given UIInputExpression so that the web component
     * can receive the value.
     *
     * @param expression The expression object.
     * @param param The parameter received for the input expression.
     */
    public static void set(UIInputExpression expression, HttpReqParam param)
    {
        if (param != null)
        {
            if (expression != null && expression.isValid())
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
        return Collections.emptyList();
    }

    /**
     * Gets all the UIEvents in this widget.
     *
     * @return A list of the UIInputExpressions available in this widget.
     */
    public List<UIEvent> events()
    {
        return Collections.emptyList();
    }

    /**
     * Gets all child widget for this widget.
     *
     * @return A list of all child widget.
     */
    public List<? extends Widget> childs()
    {
        return Collections.emptyList();
    }

    /**
     * Gets the list of resources used by this widget.
     *
     * @return A list with the names of the resources used by this widget.
     */
    public List<String> resources()
    {
        return Collections.emptyList();
    }

    /**
     * Reads the input recursively for this and all the child widgets sent in
     * the given http request.
     *
     * @param req The HTTP request to read the input from.
     */
    public void readInput(HttpBridletRequest req)
    {
        inputs().forEach((input) -> set(input, req.getPostParameter(input.getParameter())));
        childs().forEach((widget) -> widget.readInput(req));
    }

    /**
     * Perform the override recursively of all the placeholder in this view with
     * the given definesMap.
     *
     * @param definesMap The map of defines objects for the place holder
     * replacement.
     */
    public void override(Map<String, Defines> definesMap)
    {
        doOverride(definesMap);
        childs().forEach((widget) -> widget.override(definesMap));
    }

    /**
     * Utility method to override all placeholders in the given list of widgets.
     *
     * @param children The children that may contain the placeholder object.
     * @param definesMap The map with the source objects.
     * @return The resulting list.
     */
    public static List<Widget> doOverride(List<Widget> children, Map<String, Defines> definesMap)
    {
        List<Widget> result = new ArrayList<>();

        for (Widget widget : children)
        {
            if (widget instanceof WidgetPlaceHolder)
            {
                WidgetPlaceHolder ph = (WidgetPlaceHolder) widget;
                Defines def = definesMap.get(ph.getName());
                if (def != null && def.getWidgets() != null)
                {
                    result.addAll(def.getWidgets());
                }
            }
            else
            {
                result.add(widget);
            }
        }

        return result;
    }

    /**
     * Utility method to override all placeholders in the given list of widgets.
     *
     * @param child The widget to evaluate if it can be replace.
     * @param definesMap The map with the source objects.
     * @return The resulting widget.
     */
    public static Widget doOverride(Widget child, Map<String, Defines> definesMap)
    {
        Widget result = null;

        if (child instanceof WidgetPlaceHolder)
        {
            WidgetPlaceHolder ph = (WidgetPlaceHolder) child;
            Defines def = definesMap.get(ph.getName());
            if (def != null && def.getWidget() != null)
            {
                result = def.getWidget();
            }
        }
        else
        {
            result = child;
        }

        return result;
    }

    /**
     * This method must be implemented by all widgets in order for it to
     * override all its placeholder objects.
     *
     * @param definesMap The source map for the placeholders.
     */
    public abstract void doOverride(Map<String, Defines> definesMap);
}
