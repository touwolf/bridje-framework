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

import java.util.ArrayList;
import java.util.List;
import org.bridje.http.HttpReqParam;

class HttpReqParamImpl implements HttpReqParam
{
    private final String name;
    
    private final List<String> values;

    public HttpReqParamImpl(String name)
    {
        this.name = name;
        this.values = new ArrayList<>();
    }

    public HttpReqParamImpl(String name, List<String> values)
    {
        this.name = name;
        this.values = values;
    }
    
    public void addValue(String value)
    {
        this.values.add(value);
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isSingle()
    {
        return (values == null || values.size() <= 1);
    }

    @Override
    public boolean isMultiple()
    {
        return !isSingle();
    }

    @Override
    public String getValue()
    {
        if(values == null || values.size() < 1)
        {
            return null;
        }
        return values.get(0);
    }

    @Override
    public String[] getAllValues()
    {
        String[] arr = new String[values.size()];
        return values.toArray(arr);
    }

    @Override
    public boolean isEmpty()
    {
        if(values == null || values.size() < 1)
        {
            return true;
        }
        return values.get(0).trim().isEmpty();
    }
}
