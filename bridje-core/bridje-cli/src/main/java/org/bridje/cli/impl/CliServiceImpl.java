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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.cli.Argument;
import org.bridje.cli.CliService;
import org.bridje.cli.CliSession;
import org.bridje.cli.Command;
import org.bridje.cli.CommandInfo;
import org.bridje.cli.CommandParser;
import org.bridje.cli.Option;
import org.bridje.cli.StdSystemSession;
import org.bridje.cli.exceptions.InvalidCliCommandException;
import org.bridje.cli.exceptions.NoCliParserException;
import org.bridje.cli.exceptions.NoSuchCommandException;
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
class CliServiceImpl implements CliService
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
        commands = new HashMap<>();
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
    public void execute(String command, CliSession session) throws NoCliParserException, InvalidCliCommandException, NoSuchCommandException
    {
        if(session == null)
        {
            session = new StdSystemSession();
        }
        if(parser == null)
        {
            throw new NoCliParserException();
        }
        CommandInfo cmd = parser.parse(command, session);
        CommandMethodInfo inf = commands == null ? null : commands.get(cmd.getName());
        if(inf != null)
        {
            Method method = inf.getMethod();
            Object[] parameters = findParameters(method, cmd, session);
            try
            {
                method.invoke(inf.getComponent(), parameters);
            }
            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        else
        {
            throw new NoSuchCommandException(cmd.getName());
        }
    }

    private Object[] findParameters(Method method, CommandInfo cmd, CliSession session)
    {
        Object[] result = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();
        List<String> argumentsValues = new LinkedList<>(Arrays.asList(cmd.getArguments()));
        for (int i = 0; i < parameters.length; i++)
        {
            Parameter parameter = parameters[i];
            result[i] = findParameter(parameter, cmd, argumentsValues, session);
        }
        return result;
    }

    private Object findParameter(Parameter parameter, CommandInfo cmd, List<String> argumentsValues, CliSession session)
    {
        if(parameter.getType() == CliSession.class)
        {
            return session;
        }
        Option option = parameter.getAnnotation(Option.class);
        Argument argument = parameter.getAnnotation(Argument.class);
        if(option != null)
        {
            return cmd.hasOption(option.value());
        }
        else if(argument != null && argumentsValues.size() > 0)
        {
            String arg = argumentsValues.get(0);
            argumentsValues.remove(0);
            return arg;
        }
        return null;
    }
    
}
