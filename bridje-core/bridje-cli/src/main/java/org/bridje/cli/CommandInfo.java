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

package org.bridje.cli;

/**
 *
 * @author Gilberto
 */
public class CommandInfo
{
    private final String name;

    private final String[] options;

    private final String[] arguments;

    public CommandInfo(String name, String[] options, String[] arguments)
    {
        this.name = name;
        this.options = options;
        this.arguments = arguments;
    }
    
    public String getName()
    {
        return name;
    }

    public String[] getOptions()
    {
        return options;
    }

    public String[] getArguments()
    {
        return arguments;
    }

    public boolean hasOption(String value)
    {
        for (String option : options)
        {
            if(option.equalsIgnoreCase(value))
            {
                return true;
            }
        }
        return false;
    }
}
