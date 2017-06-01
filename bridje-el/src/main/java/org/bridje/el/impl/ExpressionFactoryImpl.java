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

import de.odysseus.el.misc.TypeConverter;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

@Component
class ExpressionFactoryImpl extends ExpressionFactory
{
    @Inject
    private TypeConverter typeConverter;
    
    private ExpressionFactory factory;

    @PostConstruct
    public void init()
    {
        Properties prop = new Properties();
        prop.setProperty("javax.el.methodInvocations", "true");
        factory = new de.odysseus.el.ExpressionFactoryImpl(de.odysseus.el.ExpressionFactoryImpl.Profile.JEE6, prop, typeConverter);
    }
    
    @Override
    public Object coerceToType(Object obj, Class<?> targetType)
    {
        return factory.coerceToType(obj, targetType);
    }

    @Override
    public MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes)
    {
        return factory.createMethodExpression(context, expression, expectedReturnType, expectedParamTypes);
    }

    @Override
    public ValueExpression createValueExpression(ELContext context, String expression, Class<?> expectedType)
    {
        return factory.createValueExpression(context, expression, expectedType);
    }

    @Override
    public ValueExpression createValueExpression(Object instance, Class<?> expectedType)
    {
        return factory.createValueExpression(instance, expectedType);
    }
}
