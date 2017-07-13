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

package org.bridje.web.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bridje.el.ElService;
import org.bridje.ioc.Ioc;

class WebMethodData
{
    private static final Logger LOG = Logger.getLogger(WebMethodData.class.getName());

    private final String expression;

    private final Class<?> component;
    
    private final Method method;

    private final String regExp;

    private final Pattern pattern;
    
    private final ElService elServ;

    public WebMethodData(String expression, Class<?> component, Method method)
    {
        this.expression = expression;
        this.component = component;
        this.method = method;
        this.regExp = toRegExp(expression);
        this.elServ = Ioc.context().find(ElService.class);
        pattern = Pattern.compile(regExp);
    }

    public String getExpression()
    {
        return expression;
    }

    public Class<?> getComponent()
    {
        return component;
    }

    public Method getMethod()
    {
        return method;
    }

    public String getRegExp()
    {
        return regExp;
    }
    
    public Object[] matches(String path)
    {
        Parameter[] params = this.method.getParameters();
        Matcher matcher = pattern.matcher(path);
        if(matcher.matches())
        {
            if(matcher.groupCount() >= 1)
            {
                Object[] result = new Object[matcher.groupCount()];
                for(int i = 0; i < matcher.groupCount(); i++)
                {
                    String value = urlDecode(matcher.group(i+1));
                    if(params[i] != null)
                    {
                        result[i] = doCast(value, params[i].getType());
                    }
                    else
                    {
                        result[i] = value;
                    }
                }
                return result;
            }
            return new Object[0];
        }
        return null;
    }

    public static String toRegExp(String path)
    {
        String result = path;
        result = result.replaceAll("[\\/]", "\\\\/");
        result = result.replaceAll("\\$\\{[a-zA-Z0-9]+\\}", "([^\\\\/]+)");
        result = "^" + result + "$";
        return result;
    }

    private Object doCast(String value, Class<?> type)
    {
        return elServ.convert(value, type);
    }

    private String urlDecode(String group)
    {
        try
        {
            return URLDecoder.decode(group, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return group;
    }
}
