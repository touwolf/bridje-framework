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

package org.bridje.core.impl.web;

import java.lang.reflect.Method;

class WebMethodInf
{
    private Class<?> compClass;
    
    private Method method;

    private String url;

    public WebMethodInf(Class<?> compClass, Method method, String url)
    {
        this.compClass = compClass;
        this.method = method;
        this.url = url;
    }

    public Class<?> getCompClass()
    {
        return compClass;
    }

    public void setCompClass(Class<?> compClass)
    {
        this.compClass = compClass;
    }

    public Method getMethod()
    {
        return method;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
