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

package org.bridje.posix.cli;

import java.util.ArrayList;
import java.util.List;
import org.bridje.cli.CliSession;
import org.bridje.cli.CommandInfo;
import org.bridje.cli.CommandParser;
import org.bridje.cli.exceptions.InvalidCliCommandException;
import org.bridje.ioc.annotations.Component;

/**
 *
 * @author Gilberto
 */
@Component
public class PostfixCliParserImpl implements CommandParser
{

    @Override
    public CommandInfo parse(String command, CliSession session) throws InvalidCliCommandException
    {
        String[] args = command.split(" ");
        if(args == null || args.length == 0)
        {
            throw new InvalidCliCommandException(null);
        }

        String name = null;
        List<String> options = new ArrayList<>();
        List<String> argsList = new ArrayList<>();
        for (String arg : args)
        {
            if(arg == null || arg.trim().isEmpty())
            {
                throw new InvalidCliCommandException(arg);
            }

            if(name == null)
            {
                name = arg.trim();
            }
            else
            {
                if(arg.trim().startsWith("-"))
                {
                    String opts = arg.trim().substring(1);
                    for(int i = 0; i < opts.length(); i++)
                    {
                        options.add(String.valueOf(opts.charAt(i)));
                    }
                }
                else
                {
                    argsList.add(arg.trim());
                }
            }
        }
        
        String[] optsArr = new String[options.size()];
        String[] argsArr = new String[argsList.size()];
        options.toArray(optsArr);
        argsList.toArray(argsArr);
        return new CommandInfo(name, optsArr, argsArr);
    }
    
}
