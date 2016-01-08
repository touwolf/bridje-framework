/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.core.impl.cast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.bridje.core.cast.CastProvider;
import org.bridje.core.cast.CastService;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

/**
 *
 */
@Component
class CastServiceImpl implements CastService
{
    private Map<Class<?>,List<CastProvider<?>>> destClsProvidersMap;
    
    @Inject
    private CastProvider[] providers;
    
    @PostConstruct
    public void init()
    {
        destClsProvidersMap = new HashMap<>();
        for (CastProvider provider : providers)
        {
            Class toClass = provider.getDestClass();
            List<CastProvider<?>> lst = destClsProvidersMap.get(toClass);
            if(lst == null)
            {
                lst = new ArrayList<>();
                destClsProvidersMap.put(toClass, lst);
            }
            lst.add(provider);
        }
    }
    
    @Override
    public <T> T cast(Object object, Class<T> cls)
    {
        if(object == null)
        {
            return null;
        }
        if(cls.isAssignableFrom(object.getClass()))
        {
            return (T)object;
        }
        List<CastProvider<T>> providerLst = findProvidersByDestClass(cls);
        if(providerLst == null || providerLst.isEmpty())
        {
            return null;
        }
        CastProvider<T> provider = findProviderBySrcClass(object.getClass(), providerLst);
        if(provider != null)
        {
            return provider.cast(object);
        }
        return null;
    }

    private <T> List<CastProvider<T>> findProvidersByDestClass(Class<T> destClass)
    {
        List<CastProvider<?>> lst = destClsProvidersMap.get(destClass);
        if(lst == null)
        {
            return null;
        }
        List<CastProvider<T>> result = new ArrayList<>(lst.size());
        for (CastProvider<?> cp : lst)
        {
            result.add((CastProvider<T>)cp);
        }
        return result;
    }

    private <T> CastProvider<T> findProviderBySrcClass(Class<? extends Object> srcClass, List<CastProvider<T>> providerLst)
    {
        for (CastProvider<T> provider : providerLst)
        {
            Class<?>[] srcClss = provider.getSrcClasses();
            for (Class<?> srcCls : srcClss)
            {
                if(srcCls.equals(srcClass))
                {
                    return provider;
                }
            }
        }
        return null;
    }
    
}
