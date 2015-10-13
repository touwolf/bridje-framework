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

package org.bridje.boot.cmds;

import org.bridje.cli.Argument;
import org.bridje.cli.Command;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Inject;
import org.bridje.procs.ProcessInstance;
import org.bridje.procs.ProcessService;
/**
 *
 * @author gilberto
 */
@Component
public class ProcessCmds
{
    @Inject
    private ProcessService procServ;
    
    @Command("start")
    public void start(@Argument String processName)
    {
        ProcessInstance procInst = procServ.findProcess(processName);
        if(procInst != null)
        {
            procInst.start();
        }
        else
        {
            System.out.println("No such process " + processName);
        }
    }
    
    @Command("stop")
    public void stop(@Argument String processName)
    {
        ProcessInstance procInst = procServ.findProcess(processName);
        if(procInst != null)
        {
            procInst.start();
        }
        else
        {
            System.out.println("No such process " + processName);
        }
    }
}
