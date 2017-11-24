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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.el.ElEnvironment;
import org.bridje.http.HttpReqParam;
import org.bridje.http.UploadedFile;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.web.view.Defines;
import org.bridje.web.view.EventResult;

/**
 * Base class for all the controls.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class Control
{
    private static final Logger LOG = Logger.getLogger(Control.class.getName());
    
    @XmlAttribute
    private UIExpression id;

    @XmlAttribute
    private UIExpression visible;

    /**
     * Evaluates the given expression in the current ElEnvironment.
     *
     * @param <T>         The type for the result.
     * @param expression  The expression to evaluate.
     * @param resultClass The class for the result.
     * @param def         The default value.
     *
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
     *
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
     * @param param      The parameter received for the input expression.
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
     * @param file       The uploaded file received in the request for this
     *                   expression.
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
     * Control identificator.
     * 
     * @return The control identificator.
     */
    public String getId()
    {
        return get(id, String.class, null);
    }

    /**
     * If the control is visible.
     * 
     * @return If the control is visible.
     */
    public Boolean getVisible()
    {
        return get(visible, Boolean.class, false);
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
     * @return A list of the UIInputExpressions for uploaded files available in
     *         this control.
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
     * Finds a control recursively by id.
     * 
     * @param env The EL environment.
     * @param id The control id.
     * @param callback The callback when the control is found.
     * @return The control found.
     */
    public Control findById(ElEnvironment env, String id, ControlCallback callback)
    {
        if(getVisible() != null && getVisible())
        {
            if(id == null || id.isEmpty()) return null;
            return doFindById(env, id, callback);
        }
        return null;
    }

    /**
     * Reads the input recursively for this and all the child controls sent in
     * the given HTTP request.
     *
     * @param req The HTTP request to read the input from.
     * @param env The EL environment to write the data to.
     */
    public void readInput(ControlInputReader req, ElEnvironment env)
    {
        if(getVisible() != null && getVisible())
        {
            doReadInput(req, env);
        }
    }

    /**
     * Executes any event sended from the client to the server.
     *
     * @param req The HTTP request.
     * @param env The EL environment.
     *
     * @return The event result.
     */
    public EventResult executeEvent(ControlInputReader req, ElEnvironment env)
    {
        if(getVisible() != null && getVisible())
        {
            doExecuteEvent(req, env);
        }
        return null;
    }

    /**
     * Finds a control recursively by id.
     * 
     * @param env The EL environment.
     * @param id The control id.
     * @param callback The callback when the control is found.
     * @return The control found.
     */
    protected final Control doFindById(ElEnvironment env, String id, ControlCallback callback)
    {
        if(id.equals(getId()))
        {
            callback.process(this);
            return this;
        }
        for (Control control : childs())
        {
            Control result = control.findById(env, id, callback);
            if (result != null) return result;
        }
        return null;
    }

    /**
     * Reads the input recursively for this and all the child controls sent in
     * the given HTTP request.
     *
     * @param req The HTTP request to read the input from.
     * @param env The EL environment to write the data to.
     */
    protected final void doReadInput(ControlInputReader req, ElEnvironment env)
    {
        inputFiles().stream().forEachOrdered(inputFile -> set(inputFile, req.popUploadedFile(inputFile.getParameter())));
        inputs().stream().forEachOrdered(input -> set(input, req.popParameter(input.getParameter())));
        childs().forEach(control -> control.readInput(req, env));
    }

    /**
     * Executes any event sended from the client to the server.
     *
     * @param req The HTTP request.
     * @param env The EL environment.
     *
     * @return The event result.
     */
    protected final EventResult doExecuteEvent(ControlInputReader req, ElEnvironment env)
    {
        for (UIEvent event : events())
        {
            if (eventTriggered(req, event)) return invokeEvent(event);
        }
        for (Control control : childs())
        {
            EventResult result = control.executeEvent(req, env);
            if (result != null) return result;
        }
        return null;
    }

    /**
     * Invokes the given event.
     *
     * @param event The event to be invoked.
     *
     * @return The result of the event invocation.
     */
    public EventResult invokeEvent(UIEvent event)
    {
        return Thls.doAs(new ThlsAction<EventResult>()
        {
            @Override
            public EventResult execute()
            {
                try
                {
                    Object res = event.invoke();
                    if (res instanceof EventResult)
                    {
                        return (EventResult) res;
                    }
                    return EventResult.of(null, null, res, null);
                }
                catch (ELException e)
                {
                    if (e.getCause() != null && e.getCause() instanceof Exception)
                    {
                        Exception real = (Exception) e.getCause();
                        LOG.log(Level.SEVERE, real.getMessage(), real);
                        return EventResult.error(real.getMessage(), real);
                    }
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                    return EventResult.error(e.getMessage(), e);
                }
                catch (Exception e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                    return EventResult.error(e.getMessage(), e);
                }
            }

        }, UIEvent.class, event);
    }

    /**
     * Determines if the given event was triggered .
     * 
     * @param req   The request.
     * @param event The event to be invoked.
     * 
     * @return true if the given event was triggered, false otherwise.
     */
    public boolean eventTriggered(ControlInputReader req, UIEvent event)
    {
        HttpReqParam param = req.popParameter(event.getParameter());
        if (param != null) return "t".equals(param.getValue());
        return false;
    }

    /**
     * Perform the override recursively of all the placeholder in this view with
     * the given definesMap.
     *
     * @param definesMap The map of defines objects for the place holder
     *                   replacement.
     */
    public void override(Map<String, Defines> definesMap)
    {
        doOverride(definesMap);
        childs().forEach((control) -> control.override(definesMap));
    }

    /**
     * Utility method to override all placeholders in the given list of
     * controls.
     *
     * @param children   The children that may contain the placeholder object.
     * @param definesMap The map with the source objects.
     *
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
     * Utility method to override all placeholders in the given list of
     * controls.
     *
     * @param child      The control to evaluate if it can be replace.
     * @param definesMap The map with the source objects.
     *
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
