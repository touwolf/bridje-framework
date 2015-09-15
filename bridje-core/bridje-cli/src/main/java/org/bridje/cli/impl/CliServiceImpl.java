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

package org.bridje.cli.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.cli.CliService;
import org.bridje.cli.Command;
import org.bridje.cli.CommandInfo;
import org.bridje.cli.CommandParser;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Inject;
import org.bridje.ioc.annotations.PostConstruct;

/**
 *
 * @author Gilberto
 */
@Component
public class CliServiceImpl implements CliService
{
    private static final Logger LOG = Logger.getLogger(CliServiceImpl.class.getName());

    @Inject
    private CommandParser parser;
    
    @Inject
    private IocContext context;
    
    private Map<String, CommandMethodInfo> commands;

    @PostConstruct
    public void init()
    {
        context.getClassRepository()
                .navigateAnnotMethods(Command.class, 
                        (Method method, Class component, Command annotation) ->
                        {
                            Object componentObj = Ioc.context().find(component);
                            CommandMethodInfo inf = new CommandMethodInfo(annotation.value(), method, componentObj);
                            commands.put(annotation.value(), inf);
                        });
    }
    
    @Override
    public void execute(String[] args)
    {
        CommandInfo cmd = parser.parse(args);
        CommandMethodInfo inf = commands.get(cmd.getName());
        if(inf != null)
        {
            Method method = inf.getMethod();
            Object[] parameters = findParameters(method, cmd);
            try
            {
                method.invoke(inf.getComponent(), parameters);
            }
            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private Object[] findParameters(Method method, CommandInfo cmd)
    {
        Object[] result = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters)
        {
            //TODO
        }
        return result;
    }
    
}
