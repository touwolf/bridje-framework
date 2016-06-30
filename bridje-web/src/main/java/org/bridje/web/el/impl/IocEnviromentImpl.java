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

package org.bridje.web.el.impl;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;
import org.bridje.web.el.ElEnviroment;

class IocEnviromentImpl implements ElEnviroment
{
    private final IocElContext context;

    private final ExpressionFactory factory;
    
    public IocEnviromentImpl(IocContext context)
    {
        this.factory = Ioc.context().find(ExpressionFactoryImpl.class);
        this.context = new IocElContext(context, factory);
    }

    @Override
    public <T> T get(String expression, Class<T> resultCls)
    {
        ValueExpression valueExp = factory.createValueExpression(context, expression, resultCls);
        return (T)valueExp.getValue(context);
    }

    @Override
    public <T> void set(String expression, T value)
    {
        ValueExpression valueExp = factory.createValueExpression(context, expression, value.getClass());
        valueExp.setValue(context, value);
    }

    @Override
    public <T> void setVar(String name, T value)
    {
        if(value == null)
        {
            context.setVariable(name, null);
        }
        else
        {
            context.setVariable(name, factory.createValueExpression(value, value.getClass()));
        }
    }

    @Override
    public <T> T getVar(String name, Class<T> resultCls)
    {
        return get("${" + name + "}", resultCls);
    }
}
