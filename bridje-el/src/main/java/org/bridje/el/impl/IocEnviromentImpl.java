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

package org.bridje.el.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import org.bridje.el.ElEnvironment;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;

class IocEnviromentImpl implements ElEnvironment
{
    private static final Logger LOG = Logger.getLogger(IocEnviromentImpl.class.getName());

    private final IocElContext context;

    private final ExpressionFactory factory;
    
    private final Map<String, Stack<ValueExpression>> varsStack;

    public IocEnviromentImpl(IocContext context)
    {
        this.factory = Ioc.context().find(ExpressionFactoryImpl.class);
        this.context = new IocElContext(context, factory);
        this.varsStack = new HashMap<>();
    }

    @Override
    public <T> T get(String expression, Class<T> resultCls)
    {
        ValueExpression valueExp = factory.createValueExpression(context, expression, resultCls);
        return resultCls.cast(valueExp.getValue(context));
    }

    @Override
    public <T> void set(String expression, T value)
    {
        Class<?> cls = Object.class;
        if (value != null)
        {
            cls = value.getClass();
        }
        ValueExpression valueExp = factory.createValueExpression(context, expression, cls);
        try
        {
            valueExp.setValue(context, value);
        }
        catch (javax.el.PropertyNotFoundException e)
        {
            LOG.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public <T> void pushVar(String name, T value)
    {
        Stack<ValueExpression> stack = varsStack.computeIfAbsent(name, k -> new Stack<>());
        ValueExpression exp = null;
        if(value != null) exp = factory.createValueExpression(value, value.getClass());
        stack.push(exp);
        context.setVariable(name, exp);
    }

    @Override
    public void popVar(String name)
    {
        Stack<ValueExpression> stack = varsStack.get(name);
        ValueExpression exp = null;
        if(stack != null)
        {
            if( !stack.empty() ) stack.pop();
            if( !stack.empty() ) exp = stack.peek();
            if( stack.empty() ) varsStack.remove(name);
        }
        context.setVariable(name, exp);
    }

    @Override
    public <T> T getVar(String name, Class<T> resultCls)
    {
        if(!varExists(name)) return null;
        return get("${" + name + "}", resultCls);
    }

    @Override
    public String getVarAsString(String name)
    {
        if(!varExists(name)) return "";
        return get("${" + name + "}", String.class);
    }

    @Override
    public boolean varExists(String name)
    {
        return varsStack.containsKey(name);
    }
}
