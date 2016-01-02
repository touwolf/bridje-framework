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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.codec.digest.DigestUtils;
import org.bridje.core.el.ElEnviroment;
import org.bridje.core.tls.Tls;

@XmlJavaTypeAdapter(UIExpressionAdapter.class)
public class UIExpression
{
    private static final Logger LOG = Logger.getLogger(UIExpression.class.getName());

    private final String expression;

    private UIExpression(String expression)
    {
        this.expression = expression;
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
    
    public String getFieldName()
    {
        String ie = expression;
        if(ie != null && ie.trim().length() > 3)
        {
            ie = ie.substring(2, ie.length() - 1);
            String[] str = ie.split("[.]");
            if(str.length > 0)
            {
                return str[str.length - 1];
            }
        }
        return null;
    }

    public boolean isInputExpression()
    {
        if (expression != null && !expression.trim().isEmpty())
        {
            return (expression.lastIndexOf("${") == 0 && expression.indexOf("}") == (expression.length() - 1));
        }
        return false;
    }

    public <T> T get(Class<T> resultCls, T ifNull)
    {
        try
        {
            if(expression == null)
            {
                return ifNull;
            }

            T result = env().get(expression, resultCls);
            if(result == null)
            {
                return ifNull;
            }
            return result;
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return ifNull;
    }
    
    public <T> void set(Object value)
    {
        env().set(expression, value);
    }

    public static <T> T getVar(String varName, Class<T> resultCls, T ifNull)
    {
        if(varName == null)
        {
            return ifNull;
        }

        T result = env().getVar(varName, resultCls);
        if(result == null)
        {
            return ifNull;
        }
        return result;
    }

    public static <T> void setVar(String varName, Object value)
    {
        env().setVar(varName, value);
    }

    public static ElEnviroment env()
    {
        return Tls.get(ElEnviroment.class);
    }
    
    public static UIExpression parse(String expression)
    {
        return new UIExpression(expression);
    }

    private static String encode(String expression)
    {
        return DigestUtils.md5Hex(expression);
    }
}
