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

package org.bridje.core.web.view;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.codec.digest.DigestUtils;

@XmlJavaTypeAdapter(UIEventAdapter.class)
public class UIEvent
{
    private final String expression;

    private final String[] objPath;
    
    private final String methodName;

    private final String[] params;

    private UIEvent(String expression, String[] objPath, String methodName, String[] params)
    {
        this.expression = expression;
        this.objPath = objPath;
        this.methodName = methodName;
        this.params = params;
    }
    
    public String[] getObjPath()
    {
        return objPath;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public String[] getParams()
    {
        return params;
    }

    public String getExpression()
    {
        return expression;
    }

    public String getEncodecExpression()
    {
        if(expression != null && !expression.trim().isEmpty())
        {
            return encode(expression);
        }
        return null;
    }
    
    public static UIEvent parse(String eventExpression)
    {
        if(eventExpression == null || eventExpression.trim().isEmpty())
        {
            return null;
        }
        
        if(!(eventExpression.lastIndexOf("${") == 0 && eventExpression.indexOf("}") == (eventExpression.length() - 1)))
        {
            return null;
        }
        
        if(!eventExpression.endsWith(")"))
        {
            return null;
        }
        
        String[] split = eventExpression.split("\\(");
        if(split.length != 2)
        {
            return null;
        }
        
        String pathObjAndMethod = split[0];
        String params = split[1];
        params = params.substring(0, params.length() - 1);
        String[] pathSplit = pathObjAndMethod.split("[.]");
        String[] objPath = new String[pathSplit.length - 1];
        System.arraycopy(pathSplit, 0, objPath, 0, pathSplit.length - 1);
        String methodName = pathSplit[pathSplit.length - 1];
        return new UIEvent(eventExpression, objPath, methodName, params.split(","));
    }
    
    private static String encode(String expression)
    {
        return DigestUtils.md5Hex(expression);
    }
}
