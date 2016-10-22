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

package org.bridje.web.view.state;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.el.ElService;
import org.bridje.http.HttpReqParam;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.web.WebScope;

/**
 * The manager for the states of the web views, this component can create the
 * state map out of the given web request context.
 */
@Component
public class StateManager
{
    private static final Logger LOG = Logger.getLogger(StateManager.class.getName());

    @Inject
    private ElService elServ;

    private Map<Class<?>, Map<Field, String>> stateFields;

    /**
     * Creates a new map with the current state of the web request. All the
     * components that were created during the request that have fields annoted
     * as StateFields will be serialized to the result map, so it can be
     * persisted across multiple HTTP requests.
     *
     * @param ctx The current web request context.
     *
     * @return A map with the names of the fields and the serialized values.
     */
    public Map<String, String> createViewState(IocContext<WebScope> ctx)
    {
        if (stateFields == null)
        {
            initStateFields(ctx);
        }
        Map<Class<?>, Object> comps = ctx.find(StateListener.class).getStateComps();
        Map<String, String> result = new LinkedHashMap<>();

        if (comps != null)
        {
            comps.forEach((c, i) ->
            {
                Map<Field, String> map = stateFields.get(c);
                if (map != null)
                {
                    map.forEach((field, state) ->
                    {
                        try
                        {
                            Object val = field.get(i);
                            String cv = elServ.convert(val, String.class);
                            if (cv != null)
                            {
                                result.put(state, cv);
                            }
                        }
                        catch (IllegalArgumentException | IllegalAccessException e)
                        {
                            LOG.log(Level.SEVERE, e.getMessage(), e);
                        }
                    });
                }
            });
        }

        return result;
    }

    /**
     * Injects the state of the HTTP request send to the server, into the given
     * component.
     *
     * @param ctx      The IocContext for the web request.
     * @param clazz    The component class.
     * @param instance The component instance.
     * @param req      The web request scope.
     */
    protected void injectState(IocContext<WebScope> ctx, Class<Object> clazz, Object instance, WebScope req)
    {
        if (stateFields == null)
        {
            initStateFields(ctx);
        }
        Map<Field, String> lst = stateFields.get(clazz);
        if (lst != null)
        {
            lst.forEach((field, stateName) ->
            {
                try
                {
                    HttpReqParam value = req.getPostParameter("__state." + stateName);
                    if (value != null)
                    {
                        Object cv = elServ.convert(value, field.getType());
                        if (cv != null)
                        {
                            field.set(instance, cv);
                        }
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            });
        }
    }

    private String findStateFieldName(Class<Object> clazz, Field field, StateField stateField)
    {
        return clazz.getName().replaceAll("\\.", "_") + field.getName();
    }

    private synchronized void initStateFields(IocContext<WebScope> ctx)
    {
        if (stateFields == null)
        {
            Map<Class<?>, Map<Field, String>> result = new HashMap<>();
            ctx.getClassRepository().forEachField(StateField.class, (field, component, annot) ->
            {
                Map<Field, String> lst = result.get(component);
                if (lst == null)
                {
                    lst = new HashMap<>();
                    result.put(component, lst);
                }
                field.setAccessible(true);
                String name = findStateFieldName(component, field, annot);
                lst.put(field, name);
            });
            stateFields = result;
        }
    }

}
