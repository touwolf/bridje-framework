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

package org.bridje.http.impl;

import java.util.HashMap;
import java.util.Map;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;

/**
 *
 */
class HttpBridletContextImpl implements HttpBridletContext
{
    private final Map<Class<?>, Object> dataMap;

    private HttpBridletRequest request;
    
    private HttpBridletResponse response;
    
    public HttpBridletContextImpl()
    {
        this.dataMap = new HashMap<>();
    }

    @Override
    public <T> T get(Class<T> cls)
    {
        return (T)dataMap.get(cls);
    }

    @Override
    public <T> void set(Class<T> cls, T data)
    {
        dataMap.put(cls, data);
    }

    @Override
    public HttpBridletRequest getRequest()
    {
        if(request == null)
        {
            request = get(HttpBridletRequest.class);
        }
        return request;
    }

    @Override
    public HttpBridletResponse getResponse()
    {
        if(response == null)
        {
            response = get(HttpBridletResponse.class);
        }
        return response;
    }
    
}
