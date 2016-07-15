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

package org.bridje.web.session;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.ContextListener;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;
import org.bridje.web.WebRequestScope;

@Component(scope = WebRequestScope.class)
class WebSessionListener implements ContextListener<Object>
{
    @Inject
    private IocContext<WebRequestScope> ctx;

    @Inject
    private WebRequestScope scope;
    
    @Inject
    private WebSessionManager sessionMang;
    
    private Map<Class<?>, Set<Field>> injectedClasses;
    
    @PostConstruct
    public void init()
    {
        injectedClasses = new HashMap<>();
    }

    @Override
    public void preCreateComponent(Class<Object> clazz)
    {
        //Empty
    }

    @Override
    public void preInitComponent(Class<Object> clazz, Object instance)
    {
        Set<Field> injectFields = sessionMang.injectValue(ctx, clazz, instance, scope.getSession());
        if(injectFields != null)
        {
            injectedClasses.put(clazz, injectFields);
        }
    }

    @Override
    public void postInitComponent(Class<Object> clazz, Object instance)
    {
        //Empty
    }

    public Map<Class<?>, Set<Field>> getInjectedClasses()
    {
        return injectedClasses;
    }
}
