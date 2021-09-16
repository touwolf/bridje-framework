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

import de.odysseus.el.util.SimpleResolver;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.el.*;
import org.bridje.el.ModelResolver;
import org.bridje.ioc.IocContext;

class IocElContext extends ELContext
{
    private static Map<String, Class<?>> MODELS;

    private final ExpressionFactory factory;

    private Functions functions;

    private Variables variables;

    private ELResolver resolver;

    private final IocContext context;

    private static Map<String, Class<?>> getModels(IocContext<?> ctx)
    {
        if(MODELS == null)
        {
            initModels(ctx);
        }
        return MODELS;
    }

    private static synchronized void initModels(IocContext<?> ctx)
    {
        if(MODELS == null)
        {
            Map<String, Class<?>> result = new HashMap<>();
            ModelResolver[] resolvers = ctx.findAll(ModelResolver.class);
            for (ModelResolver resolver : resolvers)
            {
                if(resolver != null)
                {
                    resolver.resolveAllModels(ctx, result);
                }
            }
            MODELS = result;
        }
    }

    public IocElContext(IocContext context, ExpressionFactory factory)
    {
        this.context = context;
        this.factory = factory;
    }

    public void setFunction(String prefix, String localName, Method method)
    {
        if (functions == null)
        {
            functions = new Functions(factory);
        }
        functions.setFunction(prefix, localName, method);
    }

    public ValueExpression setVariable(String name, ValueExpression expression)
    {
        if (variables == null)
        {
            variables = new Variables(context, factory);
        }
        return variables.setVariable(name, expression);
    }

    @Override
    public FunctionMapper getFunctionMapper()
    {
        if (functions == null)
        {
            functions = new Functions(factory);
        }
        return functions;
    }

    @Override
    public VariableMapper getVariableMapper()
    {
        if (variables == null)
        {
            variables = new Variables(context, factory);
        }
        return variables;
    }

    @Override
    public ELResolver getELResolver()
    {
        if (resolver == null)
        {
            CompositeELResolver cr = new CompositeELResolver();
            cr.add(new CompositeELResolver());
            cr.add(new ResourceBundleELResolver());
            cr.add(new MapELResolver(false));
            cr.add(new SimpleResolver());
            resolver = cr;
        }
        return resolver;
    }

    public void setELResolver(ELResolver resolver)
    {
        this.resolver = resolver;
    }

    static class Functions extends FunctionMapper
    {
        private final ExpressionFactory factory;

        private Map<String, Method> map = Collections.emptyMap();

        public Functions(ExpressionFactory factory)
        {
            this.factory = factory;
        }

        @Override
        public Method resolveFunction(String prefix, String localName)
        {
            return map.get(prefix + ":" + localName);
        }

        public void setFunction(String prefix, String localName, Method method)
        {
            if (map.isEmpty())
            {
                map = new HashMap<>();
            }
            map.put(prefix + ":" + localName, method);
        }
    }

    static class Variables extends VariableMapper
    {
        private final ExpressionFactory factory;

        private final Map<String, ValueExpression> map = new HashMap<>();

        private final IocContext<?> context;

        public Variables(IocContext<?> context, ExpressionFactory factory)
        {
            this.context = context;
            this.factory = factory;
        }

        @Override
        public ValueExpression resolveVariable(String variable)
        {
            if(!map.containsKey(variable))
            {
                Map<String, Class<?>> models = getModels(context);
                if(models.containsKey(variable))
                {
                    Class<?> compCls = models.get(variable);
                    Object comp = context.find(compCls);
                    ValueExpression ve = factory.createValueExpression(comp, compCls);
                    map.put(variable, ve);
                }
            }
            return map.get(variable);
        }

        @Override
        public ValueExpression setVariable(String variable, ValueExpression expression)
        {
            if(expression == null)
            {
                map.put(variable, null);
                return null;
            }
            return map.put(variable, expression);
        }
    }
}
