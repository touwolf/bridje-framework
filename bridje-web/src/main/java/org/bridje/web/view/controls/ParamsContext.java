/*
 * Copyright 2018 Bridje Framework.
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

import java.util.HashMap;
import java.util.Map;
import org.bridje.ioc.thls.Thls;
import org.bridje.web.view.ViewUtils;

class ParamsContext
{
    private final Map<String, Integer> map;

    public ParamsContext()
    {
        map = new HashMap<>();
    }

    public Integer getParam(String param)
    {
        return map.get(param);
    }

    public void addParam(String param)
    {
        Integer count = getParam(param);
        if(count == null)
        {
            map.put(param, 1);
        }
        else
        {
            map.put(param, count+1);
        }
    }
    
    public static String createParam(String param)
    {
        String result = ViewUtils.simplifyParam(param);
        ParamsContext ctx = Thls.get(ParamsContext.class);
        Integer count = ctx.getParam(result);
        ctx.addParam(result);
        if(count != null) result += count;
        return result;
    }
}
