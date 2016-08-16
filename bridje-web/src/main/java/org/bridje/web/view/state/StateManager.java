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
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.web.WebScope;

@Component
public class StateManager
{
    private static final Logger LOG = Logger.getLogger(StateManager.class.getName());
    
    @Inject
    private ElService elServ;

    private Map<Class<?>, Map<Field, String>> stateFields;

    String findStateFieldName(Class<Object> clazz, Field field)
    {
        return clazz.getName().replaceAll("\\.", "_") + field.getName();
    }

    void injectState(IocContext<WebScope> ctx, Class<Object> clazz, Object instance, WebScope req)
    {
        if(stateFields == null)
        {
            initStateFields(ctx);
        }
        Map<Field, String> lst = stateFields.get(clazz);
        if(lst != null)
        {
            lst.forEach((field, stateName) ->
            {
                try
                {
                    String value = req.getPostParameter("__state." + stateName);
                    if(value != null)
                    {
                        field.set(instance, elServ.convert(value, field.getType()));
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            });
        }
    }

    public Map<String,String> createViewState(IocContext<WebScope> ctx)
    {
        Map<Class<?>, Object> comps = ctx.find(StateListener.class).getStateComps();
        Map<String,String> result = new LinkedHashMap<>();
        
        comps.forEach((c, i) -> 
        {
            Map<Field, String> map = stateFields.get(c);
            if(map != null)
            {
                map.forEach((field, state) -> 
                {
                    try
                    {
                        Object val = field.get(i);
                        result.put(state, elServ.convert(val, String.class));
                    }
                    catch (IllegalArgumentException | IllegalAccessException e)
                    {
                        LOG.log(Level.SEVERE, e.getMessage(), e);
                    }
                });
            }
        });
        
        return result;
    }
    
    private synchronized void initStateFields(IocContext<WebScope> ctx)
    {
        if(stateFields == null)
        {
            Map<Class<?>, Map<Field, String>> result = new HashMap<>();
            ctx.getClassRepository().forEachField(StateField.class, (field, component, annot) ->
            {
                Map<Field, String> lst = result.get(component);
                if(lst == null)
                {
                    lst = new HashMap<>();
                    result.put(component, lst);
                }
                field.setAccessible(true);
                String name = findStateFieldName(component, field);
                lst.put(field, name);
            });
            stateFields = result;
        }
    }
}
