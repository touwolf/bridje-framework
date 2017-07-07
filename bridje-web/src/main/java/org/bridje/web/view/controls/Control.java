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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.el.ElEnvironment;
import org.bridje.http.HttpReqParam;
import org.bridje.http.UploadedFile;
import org.bridje.web.view.Defines;

/**
 * Base class for all the controls.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class Control
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
     * Evaluates the given expression in the current ElEnvironment.
     *
     * @param expression The expression to evaluate.
     * @return The result of the evaluation.
     */
    public static UploadedFile get(UIFileExpression expression)
    {
        if (expression != null)
        {
            return expression.get();
        }
        return null;
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
     * Sets the value of the given UIInputExpression so that the web component
     * can receive the value.
     *
     * @param expression The expression object.
     * @param file The uploaded file received in the request for this expression.
     */
    public static void set(UIFileExpression expression, UploadedFile file)
    {
        if (file != null)
        {
            if (expression != null && expression.isValid())
            {
                expression.set(file);
            }
        }
    }

    /**
     * Gets all the UIInputExpressions in this control.
     *
     * @return A list of the UIInputExpressions available in this control.
     */
    public List<UIInputExpression> inputs()
    {
        return Collections.emptyList();
    }

    /**
     * Gets all the UIInputExpressions for uploaded files in this control.
     *
     * @return A list of the UIInputExpressions for uploaded files available in this control.
     */
    public List<UIFileExpression> inputFiles()
    {
        return Collections.emptyList();
    }

    /**
     * Gets all the UIEvents in this control.
     *
     * @return A list of the UIInputExpressions available in this control.
     */
    public List<UIEvent> events()
    {
        return Collections.emptyList();
    }

    /**
     * Gets all child control for this control.
     *
     * @return A list of all child control.
     */
    public List<? extends Control> childs()
    {
        return Collections.emptyList();
    }

    /**
     * Gets the list of resources used by this control.
     *
     * @return A list with the names of the resources used by this control.
     */
    public List<String> resources()
    {
        return Collections.emptyList();
    }

    /**
     * Reads the input recursively for this and all the child controls sent in
     * the given HTTP request.
     *
     * @param req The HTTP request to read the input from.
     * @param env The EL environment to write the data to.
     */
    public void readInput(ControlImputReader req, ElEnvironment env)
    {
        inputFiles().stream().forEach(inputFile -> set(inputFile, req.getUploadedFile(inputFile.getParameter())));
        inputs().stream().forEach(input -> set(input, req.getParameter(input.getParameter())));
        childs().forEach(control -> control.readInput(req, env));
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
        childs().forEach((control) -> control.override(definesMap));
    }

    /**
     * Utility method to override all placeholders in the given list of controls.
     *
     * @param children The children that may contain the placeholder object.
     * @param definesMap The map with the source objects.
     * @return The resulting list.
     */
    public static List<Control> doOverride(List<Control> children, Map<String, Defines> definesMap)
    {
        List<Control> result = new ArrayList<>();

        for (Control control : children)
        {
            if (control instanceof ControlPlaceHolder)
            {
                ControlPlaceHolder ph = (ControlPlaceHolder) control;
                Defines def = definesMap.get(ph.getName());
                if (def != null && def.getControls() != null)
                {
                    result.addAll(def.getControls());
                }
            }
            else
            {
                result.add(control);
            }
        }

        return result;
    }

    /**
     * Utility method to override all placeholders in the given list of controls.
     *
     * @param child The control to evaluate if it can be replace.
     * @param definesMap The map with the source objects.
     * @return The resulting control.
     */
    public static Control doOverride(Control child, Map<String, Defines> definesMap)
    {
        Control result = null;

        if (child instanceof ControlPlaceHolder)
        {
            ControlPlaceHolder ph = (ControlPlaceHolder) child;
            Defines def = definesMap.get(ph.getName());
            if (def != null && def.getControl() != null)
            {
                result = def.getControl();
            }
        }
        else
        {
            result = child;
        }

        return result;
    }

    /**
     * This method must be implemented by all controls in order for it to
     * override all its placeholder objects.
     *
     * @param definesMap The source map for the placeholders.
     */
    public abstract void doOverride(Map<String, Defines> definesMap);
}
